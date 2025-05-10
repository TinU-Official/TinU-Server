package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.dto.response.ChatMessage
import com.tinuproject.tinu.domain.chat.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/chat")
@Tag(name = "채팅 메시지 api", description = "웹소켓을 사용하는 채팅 메시지 전송, 수신 api 입니다.")
class ChatMessageController (
    private val chatService: ChatService
) {
    @MessageMapping("/message/{roomId}")
    @SendTo("/room/{roomId}")
    fun sendMessage(message: ChatMessage) {
//        chatMessageService.sendMessage(message)
    }

    @MessageMapping("/status/{roomId}")
    @SendTo("/status/{roomId}")
    fun sendTypingStatus(message: ChatMessage) {
//       chatMessageService.sendTypingStatus(message)
    }

}