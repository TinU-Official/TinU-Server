package com.tinuproject.tinu.domain.chat.dto

import com.tinuproject.tinu.domain.chat.enum.MessageType

data class ChatMessage(
    var type: MessageType,
    var content: String?,
    var sender: String
)
