package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅방 생성/입장 응답")
data class CreateChatRoomResponse(
    @Schema(description = "채팅방 ID")
    val chatRoomId: Long,

    @Schema(description = "신규 생성 여부 (true=새로 생성, false=기존 방 재진입)")
    val created: Boolean
)
