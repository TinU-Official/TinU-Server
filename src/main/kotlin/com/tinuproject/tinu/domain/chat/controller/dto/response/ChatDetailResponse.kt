package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅 상세 조회 응답")
data class ChatDetailResponse(
    @Schema(description = "날짜별 메시지 그룹 목록 (과거→최신 순)")
    val messages: List<ChatMessageGroupByDateDto>
)
