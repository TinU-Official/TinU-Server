package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatMessageGroupByDateDto
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatMessageItemDto
import com.tinuproject.tinu.domain.chat.entity.ChatText
import com.tinuproject.tinu.domain.chat.exception.ChatRoomNotFoundException
import com.tinuproject.tinu.domain.chat.repository.ChatRoomMemberRepository
import com.tinuproject.tinu.domain.chat.repository.ChatRoomRepository
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
    private val chatTextRepository: ChatTextRepository
) : ChatRoomDetailService {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
    }

    /**
     * 채팅방의 모든 메시지를 한 번에 ASC 로 반환한다.
     *
     * 초기 설계는 PREV/NEXT 커서 + size 페이지네이션이었으나, 1:1 중고거래 채팅 특성상
     * 한 채팅방의 메시지 수가 페이지네이션이 의미를 가질 만큼(수천~수만 건) 쌓일 가능성이
     * 매우 낮다고 판단해 페이징을 의도적으로 제거했다. 클라이언트/서버 양쪽의 페이징 로직과
     * 테스트 비용이 평균 케이스에서 얻는 이득보다 컸다(YAGNI).
     *
     * 진입 이후 도착하는 신규 메시지는 WebSocket push 로만 수신한다. 클라이언트는
     * "WebSocket subscribe 완료 → 본 API 호출" 순서를 지킬 것 (ChatText.id 로 중복 제거).
     *
     * 운영 모니터링에서 응답 size/시간이 문제되는 채팅방이 발견되면 그때 위로 스크롤용
     * PREV cursor 만 도입한다. NEXT 는 추가하지 않는다 (WebSocket 이 그 역할).
     */
    @Transactional(readOnly = true)
    override fun getChatDetail(
        userId: UUID,
        chatRoomId: Long
    ): ChatDetailResponse {
        val member = memberRepository.findMemberByUserId(userId) ?: throw NotExistMemberException()
        val chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow { ChatRoomNotFoundException() }

        // 채팅방 미참여 → 403, 내가 나간 채팅방 → 404 (목록에 없다고 처리)
        val myChatRoomMember = chatRoomMemberRepository.findByMemberAndChatRoom(member, chatRoom)
            ?: throw ForbiddenException()
        if (myChatRoomMember.deletedAt != null) throw ChatRoomNotFoundException()

        val messages = chatTextRepository.findAllByChatRoomIdOrderByOrderAsc(chatRoomId)

        val grouped = messages
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

        return ChatDetailResponse(messages = grouped)
    }

    private fun toKst(chatText: ChatText): ZonedDateTime =
        chatText.createdAt!!.atZone(KST)
}
