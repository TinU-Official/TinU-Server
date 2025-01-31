package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.dto.ChatMessage
import com.tinuproject.tinu.domain.chat.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatMessageController (
    private val chatService: ChatService
) {
    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    fun toOneMessage(message: ChatMessage) {
        chatService. // 서비스 분리?
    }

    @MessageMapping("/notice")
    @SendTo("/notice")
    fun noticeMessage(message: ChatMessage) {
        chatService.
    }

}