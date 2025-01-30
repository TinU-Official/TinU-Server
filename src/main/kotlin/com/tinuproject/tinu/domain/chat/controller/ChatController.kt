package com.tinuproject.tinu.domain.chat.controller

import com.tinuproject.tinu.domain.chat.enum.ChatMessage
import com.tinuproject.tinu.domain.chat.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(private val chatService: ChatService) {

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    fun message(message: ChatMessage) {
//        chatService.
    }
}