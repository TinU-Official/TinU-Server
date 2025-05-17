package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.entity.ChatText
import com.tinuproject.tinu.domain.chat.repository.ChatRepository
import com.tinuproject.tinu.domain.chat.repository.ChatTextRepository
import com.tinuproject.tinu.domain.chat.service.dto.input.ChatMessageInputDto
import com.tinuproject.tinu.domain.chat.service.dto.input.ChatTypingStatusInputDto
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatMessageServiceImpl(
    private val chatTextRepository: ChatTextRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val chatRepository: ChatRepository,
    private val memberRepository: MemberRepository
) : ChatMessageService {
    @Transactional
    override fun sendMessage(chatMessageInputDto: ChatMessageInputDto) {
        // ChatText 엔티티 생성 및 저장
        val chatText = ChatText(
            chat = chatRepository.findById(chatMessageInputDto.chatId)
                .orElseThrow { IllegalArgumentException("Chat not found") },
            writer = memberRepository.findById(chatMessageInputDto.senderId)
                .orElseThrow { IllegalArgumentException("Member not found") },
            text = chatMessageInputDto.content ?: "",
            type = chatMessageInputDto.type,
            isRead = false
        )
        chatTextRepository.save(chatText)

        // STOMP를 통해 메시지 전송
        simpMessagingTemplate.convertAndSend(
            "/room/${chatMessageInputDto.chatId}",
            chatMessageInputDto
        )

        // return 0
    }

    override fun sendTypingStatus(chatTypingStatusInputDto: ChatTypingStatusInputDto) {
        // STOMP를 통해 작성 상태 전송
        simpMessagingTemplate.convertAndSend(
            "/status/${chatTypingStatusInputDto.chatId}",
            chatTypingStatusInputDto
        )

        // return 0
    }

}