package com.tinuproject.tinu.domain.chat.service.dto.output
import java.time.LocalDateTime

data class ChatListGetOutputDto(
    val id: Long,
    val thumbnail : String?,
    val buyerId: Long,
    val sellerId: Long,
    val postId: Long,
    val lastChat : String?,
    val lastTime: LocalDateTime?,
)
