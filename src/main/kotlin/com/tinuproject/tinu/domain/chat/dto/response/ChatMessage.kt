package com.tinuproject.tinu.domain.chat.dto.response

import com.tinuproject.tinu.domain.chat.enum.MessageType
import java.time.LocalDateTime

data class ChatMessage(
    var type: MessageType, // 텍스트 OR 이미지 OR 상태
    var content: String?,
    var sender: String,
    var time: String,
    var timestamp: LocalDateTime,
)
