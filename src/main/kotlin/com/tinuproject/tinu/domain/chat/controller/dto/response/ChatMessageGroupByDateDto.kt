package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "날짜별 채팅 메시지 그룹")
data class ChatMessageGroupByDateDto(
    @Schema(description = "날짜 (KST, yyyy-MM-dd)")
    val date: LocalDate,

    @Schema(description = "해당 날짜의 메시지 목록 (chat_order 오름차순)")
    val items: List<ChatMessageItemDto>
)
