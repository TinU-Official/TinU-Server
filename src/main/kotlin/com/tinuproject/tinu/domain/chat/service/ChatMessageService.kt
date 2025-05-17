package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatMessageRequestDto
import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatTypingStatusRequestDto
import com.tinuproject.tinu.domain.chat.service.dto.input.ChatMessageInputDto
import com.tinuproject.tinu.domain.chat.service.dto.input.ChatTypingStatusInputDto

interface ChatMessageService {

    fun sendMessage(chatMessageInputDto: ChatMessageInputDto)

    fun sendTypingStatus(chatTypingStatusInputDto: ChatTypingStatusInputDto)
}