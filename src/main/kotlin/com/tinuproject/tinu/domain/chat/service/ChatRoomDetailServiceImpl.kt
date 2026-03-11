package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatDetailRequest
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatMessageGroupByDateDto
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatMessageItemDto
import com.tinuproject.tinu.domain.chat.entity.ChatText
import com.tinuproject.tinu.domain.chat.enums.ChatDetailDirection
import com.tinuproject.tinu.domain.chat.exception.ChatRoomNotFoundException
import com.tinuproject.tinu.domain.chat.repository.ChatRoomMemberRepository
import com.tinuproject.tinu.domain.chat.repository.ChatRoomRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextQueryRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextRepository
import com.tinuproject.tinu.domain.member.exception.NotExistMemberException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.global.exception.ForbiddenException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@Service
class ChatRoomDetailServiceImpl(
    private val memberRepository: MemberRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val chatTextRepository: ChatTextRepository,
    private val chatTextQueryRepository: ChatTextQueryRepository
) : ChatRoomDetailService {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
    }

    @Transactional(readOnly = true)
    override fun getChatDetail(
        userId: UUID,
        chatRoomId: Long,
        request: ChatDetailRequest
    ): ChatDetailResponse {
        val member = memberRepository.findMemberByUserId(userId) ?: throw NotExistMemberException()
        val chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow { ChatRoomNotFoundException() }

        val isBuyer = chatRoom.buyer.id == member.id
        val isSeller = chatRoom.seller.id == member.id

        // 채팅방 미참여 → 403
        if (!isBuyer && !isSeller) throw ForbiddenException()

        // 내가 나간 채팅방 → 404 (목록에 없다고 처리)
        val myChatRoomMember = chatRoomMemberRepository.findByMemberAndChatRoom(member, chatRoom)
        if (myChatRoomMember?.deletedAt != null) throw ChatRoomNotFoundException()

        val actualSize = request.size.coerceIn(1, 100)

        // cursor 유효성 검사: ChatText.id → chat_order 추출
        val cursorOrder = resolveCursorOrder(request.cursor, chatRoomId)

        // size+1 조회하여 hasNext/hasPrev 판단
        val rawList = chatTextQueryRepository.findByCursor(
            chatRoomId = chatRoomId,
            cursorOrder = cursorOrder,
            direction = request.direction,
            size = actualSize + 1
        )

        val direction = if (cursorOrder == null) ChatDetailDirection.NEXT else request.direction
        val hasPrev: Boolean
        val hasNext: Boolean
        val messages: List<ChatText>

        when (direction) {
            ChatDetailDirection.PREV -> {
                hasPrev = rawList.size > actualSize
                hasNext = false
                messages = if (hasPrev) rawList.subList(0, actualSize) else rawList
            }
            ChatDetailDirection.NEXT -> {
                hasNext = rawList.size > actualSize
                hasPrev = false
                messages = if (hasNext) rawList.subList(0, actualSize) else rawList
            }
        }

        // PREV로 가져오면 desc 정렬이므로 클라이언트에 과거→최신 순으로 뒤집어서 줌
        val sortedMessages = when (direction) {
            ChatDetailDirection.PREV -> messages.sortedBy { it.order }
            ChatDetailDirection.NEXT -> messages
        }

        val prevCursor = if (hasPrev) sortedMessages.firstOrNull()?.id else null
        val nextCursor = if (hasNext) sortedMessages.lastOrNull()?.id else null

        val grouped = sortedMessages
            .groupBy { it.createdAt!!.atZone(KST).toLocalDate() }
            .entries
            .sortedBy { it.key }
            .map { (date, items) ->
                ChatMessageGroupByDateDto(
                    date = date,
                    items = items.map { chatText ->
                        ChatMessageItemDto(
                            id = chatText.id!!,
                            text = chatText.text,
                            type = chatText.type,
                            isMine = chatText.writer.id == member.id,
                            createdAt = toKst(chatText)
                        )
                    }
                )
            }

        return ChatDetailResponse(
            messages = grouped,
            prevCursor = prevCursor,
            nextCursor = nextCursor,
            hasPrev = hasPrev,
            hasNext = hasNext
        )
    }

    /**
     * cursor(ChatText.id)를 받아 해당 채팅방의 chat_order를 반환.
     * cursor가 null이거나 유효하지 않으면(다른 채팅방 소속 포함) null 반환
     * → null이면 0번째부터 NEXT 조회로 간주 (최신 size개 반환).
     */
    private fun resolveCursorOrder(cursor: Long?, chatRoomId: Long): Long? {
        if (cursor == null) return null
        val chatText = chatTextRepository.findById(cursor).orElse(null) ?: return null
        if (chatText.chatRoom.id != chatRoomId) return null
        return chatText.order
    }

    private fun toKst(chatText: ChatText): ZonedDateTime =
        chatText.createdAt!!.atZone(KST)
}
