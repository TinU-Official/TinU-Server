package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.response.ChatMessage

interface ChatMessageService {

    fun sendMessage(chatMessage: ChatMessage): ChatMessage

    fun sendTypingStatus(chatMessage: ChatMessage) : ChatMessage
}