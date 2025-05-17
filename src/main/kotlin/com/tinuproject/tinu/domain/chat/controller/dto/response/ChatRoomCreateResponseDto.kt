package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅방 생성 응답 DTO")
data class ChatRoomCreateResponseDto(
    @Schema(description = "채팅방 ID", example = "1")
    val chatId : Long?
)