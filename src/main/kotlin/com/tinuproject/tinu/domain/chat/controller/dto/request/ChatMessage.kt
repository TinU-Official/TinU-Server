package com.tinuproject.tinu.domain.chat.controller.dto.request

import com.tinuproject.tinu.domain.chat.enums.ChatType
import java.time.LocalDateTime

data class ChatMessage(
    var type: ChatType, // 텍스트 OR 이미지 OR 상태
    var content: String?,
    var sender: String,
    var time: String,
    var timestamp: LocalDateTime,
)