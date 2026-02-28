package com.tinuproject.tinu.domain.chat.controller.dto.response

import com.tinuproject.tinu.domain.chat.enums.ChatType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

@Schema(description = "채팅 메시지 항목")
data class ChatMessageItemDto(
    @Schema(description = "ChatText ID")
    val id: Long,

    @Schema(description = "메시지 내용 (IMAGE 타입이면 이미지 절대 URL)")
    val text: String,

    @Schema(description = "메시지 타입 (TEXT or IMAGE)")
    val type: ChatType,

    @Schema(description = "내가 보낸 메시지 여부")
    val isMine: Boolean,

    @Schema(description = "전송 시각 (KST)")
    val createdAt: ZonedDateTime
)
