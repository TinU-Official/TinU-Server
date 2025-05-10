package com.tinuproject.tinu.domain.chat.controller.dto.response

import java.time.LocalDateTime

data class GetListChatResponse(
    val id: Long,
    val thumbnail : String?,
    val buyerId: Long,
    val sellerId: Long,
    val postId: Long,
    val lastChat : String?,
    val lastTime: LocalDateTime?,
)
