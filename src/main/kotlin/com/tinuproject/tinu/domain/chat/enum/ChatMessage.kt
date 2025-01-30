package com.tinuproject.tinu.domain.chat.enum

data class ChatMessage(
    var type: MessageType,
    var content: String?,
    var sender: String
)
