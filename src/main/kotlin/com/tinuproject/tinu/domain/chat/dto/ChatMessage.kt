package com.tinuproject.tinu.domain.chat.dto

import com.tinuproject.tinu.domain.chat.enum.MessageType

data class ChatMessage(
    var type: MessageType, // 텍스트 OR 이미지
    var content: String?,
    var sender: String
)
