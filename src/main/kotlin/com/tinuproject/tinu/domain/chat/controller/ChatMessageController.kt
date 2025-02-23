package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.dto.response.ChatMessage
import com.tinuproject.tinu.domain.chat.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatMessageController (
    private val chatService: ChatService
) {
    @MessageMapping("/message/{roomId}")
    @SendTo("/room/{roomId}")
    fun sendMessage(message: ChatMessage) {
        chatService. // 서비스 분리?
    }

    @MessageMapping("/status/{roomId}")
    @SendTo("/status/{roomId}")
    fun sendTypingStatus(message: ChatMessage) {

    }

}