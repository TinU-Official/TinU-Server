package com.tinuproject.tinu.domain.chat.controller.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive

@Schema(description = "마지막 읽은 메시지 갱신 요청")
data class MarkAsReadRequest(
    @field:Positive
    @Schema(description = "마지막으로 읽은 ChatText.id (양수)")
    val lastReadChatId: Long
)
