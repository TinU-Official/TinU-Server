package com.tinuproject.tinu.domain.chat.controller.dto.response

import com.tinuproject.tinu.domain.chat.enums.ChatRole

data class ChatRoomInfoResponse(
    val chatRoomId: Long,
    val opponentNickname: String,
    val opponentProfileImageURL: String?,
    val opponentMemberId: Long,
    val postId: Long,
    val postTitle: String,
    val postPrice: Int,
    val postThumbnail: String?,
    val postIsSoldOut: Boolean,
    val opponentHasLeft: Boolean,
    val myRole: ChatRole
)
