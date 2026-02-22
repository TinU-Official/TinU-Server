package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListItemResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.enums.ChatRole
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.enums.ChatType
import com.tinuproject.tinu.domain.chat.repository.ChatRoomQueryRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextRepository
import com.tinuproject.tinu.domain.member.exception.NotExistMemberException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ChatRoomServiceImpl(
    private val memberRepository: MemberRepository,
    private val chatRoomQueryRepository: ChatRoomQueryRepository,
    private val chatTextRepository: ChatTextRepository
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

        var rawList = chatRoomQueryRepository.findChatRoomList(
            memberId = member.id!!,
            filter = filter,
            cursorChatRoomId = cursorChatRoomId,
            cursorLastChatAt = cursorLastChatAt,
            size = actualSize + 1
        )

        val hasNext = rawList.size > actualSize
        if (hasNext) rawList = rawList.subList(0, actualSize)

        // 안 읽은 개수: lastReadChatId → ChatText.order 배치 조회
        // unreadCount = maxOrder - lastReadOrder (order는 1부터 시작하는 채팅방 내 순차 번호)
        val lastReadChatIds = rawList.mapNotNull { it.lastReadChatId }.toSet()
        val lastReadOrderMap: Map<Long, Long> = if (lastReadChatIds.isNotEmpty()) {
            chatTextRepository.findAllByIdIn(lastReadChatIds).associate { it.id!! to it.order }
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
                myRole = if (raw.buyerId == member.id) ChatRole.BUYER else ChatRole.SELLER,
                opponentHasLeft = raw.opponentDeletedAt != null
            )
        }

        val nextCursorId = if (hasNext && rawList.isNotEmpty()) {
            val last = rawList.last()
            "${last.chatRoomId}_${last.maxOrder}"
        } else {
            ""
        }

        return ChatRoomListResponse(
            chatRooms = items,
            size = items.size,
            nextCursorId = nextCursorId
        )
    }

    /**
     * cursorId ("chatRoomId_order") 파싱 후 ChatText를 조회해 (chatRoomId, createdAt) 반환.
     * 파싱 실패 또는 ChatText 없음 → (null, null) → 첫 페이지 반환.
     */
    private fun resolveCursor(cursorId: String?): Pair<Long?, java.time.LocalDateTime?> {
        if (cursorId.isNullOrBlank()) return Pair(null, null)
        return try {
            val parts = cursorId.split("_")
            if (parts.size != 2) return Pair(null, null)
            val chatRoomId = parts[0].toLongOrNull() ?: return Pair(null, null)
            val order = parts[1].toLongOrNull() ?: return Pair(null, null)
            val cursorChatText = chatTextRepository.findByChatRoomIdAndOrder(chatRoomId, order)
                ?: return Pair(null, null)
            Pair(chatRoomId, cursorChatText.createdAt)
        } catch (_: NumberFormatException) {
            Pair(null, null)
        }
    }
}
