package com.tinuproject.tinu.domain.chat.controller.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive

@Schema(description = "채팅방 생성/입장 요청")
data class CreateChatRoomRequest(
    @field:Positive
    @Schema(description = "채팅 대상 판매글 ID (양수)")
    val postId: Long
)
