package com.tinuproject.tinu.domain.chat.dto.response

data class ChatListResponse(
    val id: Long,
    val buyerId: Long,
    val sellerId: Long,
    val postId: Long
)
