package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.request.CreateChatRoomRequest
import com.tinuproject.tinu.domain.chat.controller.dto.request.MarkAsReadRequest
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomInfoResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListItemResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.CreateChatRoomResponse
import com.tinuproject.tinu.domain.chat.entity.ChatRoom
import com.tinuproject.tinu.domain.chat.entity.ChatRoomMember
import com.tinuproject.tinu.domain.chat.enums.ChatRole
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.enums.ChatType
import com.tinuproject.tinu.domain.chat.exception.ChatReadInvalidCursorException
import com.tinuproject.tinu.domain.chat.exception.ChatRoomMemberNotFoundException
import com.tinuproject.tinu.domain.chat.exception.ChatRoomNotFoundException
import com.tinuproject.tinu.domain.chat.exception.ChatSelfChatException
import com.tinuproject.tinu.domain.chat.repository.ChatRoomMemberRepository
import com.tinuproject.tinu.domain.chat.repository.ChatRoomQueryRepository
import com.tinuproject.tinu.domain.chat.repository.ChatRoomRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextRepository
import com.tinuproject.tinu.domain.member.entity.Member
import com.tinuproject.tinu.domain.member.exception.NotExistMemberException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.post.exception.PostNotFoundException
import com.tinuproject.tinu.domain.post.repository.PostRepository
import com.tinuproject.tinu.global.exception.ForbiddenException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Service
class ChatRoomServiceImpl(
    private val memberRepository: MemberRepository,
    private val chatRoomQueryRepository: ChatRoomQueryRepository,
    private val chatTextRepository: ChatTextRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val postRepository: PostRepository
) : ChatRoomService {

    @Transactional(readOnly = true)
    override fun getChatRoomList(
        userId: UUID,
        filter: ChatRoomFilter,
        cursorId: String?,
        size: Int
    ): ChatRoomListResponse {
        val member = memberRepository.findMemberByUserId(userId) ?: throw NotExistMemberException()
        val actualSize = size.coerceIn(1, 100)

        val (cursorChatRoomId, cursorLastChatAt) = resolveCursor(cursorId)

        val rawAll = chatRoomQueryRepository.findChatRoomList(
            memberId = member.id!!,
            filter = filter,
            cursorChatRoomId = cursorChatRoomId,
            cursorLastChatAt = cursorLastChatAt,
            size = actualSize + 1
        )

        val hasNext = rawAll.size > actualSize
        val rawList = if (hasNext) rawAll.subList(0, actualSize) else rawAll

        // 안 읽은 개수: lastReadChatId → ChatText.order 배치 조회
        // unreadCount = maxOrder - lastReadOrder (order는 1부터 시작하는 채팅방 내 순차 번호)
        val lastReadChatIds = rawList.mapNotNull { it.lastReadChatId }.toSet()
        val lastReadOrderMap: Map<Long, Long> = if (lastReadChatIds.isNotEmpty()) {
            chatTextRepository.findAllById(lastReadChatIds).associate { it.id!! to it.order }
        } else {
            emptyMap()
        }

        val items = rawList.map { raw ->
            val lastReadOrder = raw.lastReadChatId?.let { lastReadOrderMap[it] } ?: 0L
            val unreadCount = (raw.maxOrder - lastReadOrder).coerceAtLeast(0L)

            ChatRoomListItemResponse(
                chatRoomId = raw.chatRoomId,
                postId = raw.postId,
                postThumbnail = raw.postThumbnail,
                postTitle = raw.postTitle,
                lastChatContent = if (raw.lastChatType == ChatType.IMAGE) "[사진]" else raw.lastChatText,
                lastChatAt = raw.lastChatAt,
                unreadCount = unreadCount,
                myRole = raw.myRole,
                opponentHasLeft = raw.opponentDeletedAt != null,
                lastReadChatId = raw.lastReadChatId
            )
        }

        val nextCursorId: String? = if (hasNext && rawList.isNotEmpty()) {
            val last = rawList.last()
            "${last.chatRoomId}_${last.maxOrder}"
        } else {
            null
        }

        return ChatRoomListResponse(
            chatRooms = items,
            size = items.size,
            nextCursorId = nextCursorId
        )
    }

    @Transactional
    override fun createOrEnterChatRoom(
        userId: UUID,
        request: CreateChatRoomRequest
    ): CreateChatRoomResponse {
        val buyer = memberRepository.findMemberByUserId(userId) ?: throw NotExistMemberException()
        val post = postRepository.findPostById(request.postId) ?: throw PostNotFoundException()
        val seller = post.author

        if (buyer.id == seller.id) throw ChatSelfChatException()

        val existingCrm = chatRoomMemberRepository.findByMemberAndChatRoom_PostAndRole(buyer, post, ChatRole.BUYER)
        if (existingCrm != null) {
            if (existingCrm.deletedAt != null) existingCrm.deletedAt = null
            return CreateChatRoomResponse(chatRoomId = existingCrm.chatRoom.id!!, created = false)
        }

        val chatRoom = chatRoomRepository.save(ChatRoom(post = post))
        chatRoomMemberRepository.save(ChatRoomMember(member = buyer, chatRoom = chatRoom, role = ChatRole.BUYER))
        chatRoomMemberRepository.save(ChatRoomMember(member = seller, chatRoom = chatRoom, role = ChatRole.SELLER))

        return CreateChatRoomResponse(chatRoomId = chatRoom.id!!, created = true)
    }

    @Transactional(readOnly = true)
    override fun getChatRoomInfo(
        userId: UUID,
        chatRoomId: Long
    ): ChatRoomInfoResponse {
        val (_, chatRoom, myCrm, isBuyer) = resolveChatRoomAccess(userId, chatRoomId)
        if (myCrm.deletedAt != null) throw ChatRoomNotFoundException()

        val opponentRole = if (isBuyer) ChatRole.SELLER else ChatRole.BUYER
        val opponentCrm = chatRoomMemberRepository.findByChatRoomAndRole(chatRoom, opponentRole)
            ?: throw ChatRoomMemberNotFoundException()
        val opponent = opponentCrm.member
        val opponentHasLeft = opponentCrm.deletedAt != null

        val post = chatRoom.post
        return ChatRoomInfoResponse(
            chatRoomId = chatRoom.id!!,
            opponentNickname = opponent.nickname ?: "",
            opponentProfileImageURL = opponent.profileImageURL,
            opponentMemberId = opponent.id!!,
            postId = post.id!!,
            postTitle = post.title,
            postPrice = post.price,
            postThumbnail = post.thumbnail,
            postIsSoldOut = post.isSoldOut,
            opponentHasLeft = opponentHasLeft,
            myRole = if (isBuyer) ChatRole.BUYER else ChatRole.SELLER
        )
    }

    @Transactional
    override fun markAsRead(
        userId: UUID,
        chatRoomId: Long,
        request: MarkAsReadRequest
    ) {
        val (_, _, myCrm, _) = resolveChatRoomAccess(userId, chatRoomId)
        if (myCrm.deletedAt != null) throw ChatRoomNotFoundException()

        val newChatText = chatTextRepository.findById(request.lastReadChatId).orElse(null)
        if (newChatText == null || newChatText.chatRoom.id != chatRoomId) {
            throw ChatReadInvalidCursorException()
        }

        if (newChatText.order <= myCrm.lastReadChatOrder) return

        myCrm.lastReadChatId = request.lastReadChatId
        myCrm.lastReadChatOrder = newChatText.order
    }

    @Transactional
    override fun leaveChatRoom(
        userId: UUID,
        chatRoomId: Long
    ) {
        val (_, _, myCrm, _) = resolveChatRoomAccess(userId, chatRoomId)
        if (myCrm.deletedAt != null) return
        myCrm.deletedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    }

    private data class ChatRoomAccess(
        val member: Member,
        val chatRoom: ChatRoom,
        val myCrm: ChatRoomMember,
        val isBuyer: Boolean
    )

    /**
     * 공통 접근 제어: member 조회 → chatRoom 조회 → myCrm 조회(없으면 403) → role로 isBuyer 결정
     * deletedAt 체크는 호출자가 직접 처리 (leaveChatRoom은 멱등, 나머지는 404)
     */
    private fun resolveChatRoomAccess(userId: UUID, chatRoomId: Long): ChatRoomAccess {
        val member = memberRepository.findMemberByUserId(userId) ?: throw NotExistMemberException()
        val chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow { ChatRoomNotFoundException() }
        val myCrm = chatRoomMemberRepository.findByMemberAndChatRoom(member, chatRoom)
            ?: throw ForbiddenException()
        val isBuyer = myCrm.role == ChatRole.BUYER
        return ChatRoomAccess(member, chatRoom, myCrm, isBuyer)
    }

    /**
     * cursorId ("chatRoomId_order") 파싱 후 ChatText를 조회해 (chatRoomId, createdAt) 반환.
     * 파싱 실패 또는 ChatText 없음 → (null, null) → 첫 페이지 반환.
     */
    private fun resolveCursor(cursorId: String?): Pair<Long?, java.time.LocalDateTime?> {
        if (cursorId.isNullOrBlank()) return Pair(null, null)
        val parts = cursorId.split("_")
        if (parts.size != 2) return Pair(null, null)
        val chatRoomId = parts[0].toLongOrNull() ?: return Pair(null, null)
        val order = parts[1].toLongOrNull() ?: return Pair(null, null)
        val cursorChatText = chatTextRepository.findByChatRoomIdAndOrder(chatRoomId, order)
            ?: return Pair(null, null)
        return Pair(chatRoomId, cursorChatText.createdAt)
    }
}
