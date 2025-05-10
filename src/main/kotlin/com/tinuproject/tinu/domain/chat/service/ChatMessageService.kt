package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatMessage

interface ChatMessageService {

    fun sendMessage(chatMessage: ChatMessage): ChatMessage

    fun sendTypingStatus(chatMessage: ChatMessage) : ChatMessage
}