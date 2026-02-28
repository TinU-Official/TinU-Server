package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅 상세 조회 응답")
data class ChatDetailResponse(
    @Schema(description = "날짜별 메시지 그룹 목록 (과거→최신 순)")
    val messages: List<ChatMessageGroupByDateDto>,

    @Schema(description = "다음 PREV(위로 스크롤) 요청 시 사용할 cursor(ChatText.id). 더 이상 과거 메시지가 없으면 null")
    val prevCursor: Long?,

    @Schema(description = "다음 NEXT(아래로 스크롤) 요청 시 사용할 cursor(ChatText.id). 더 이상 최신 메시지가 없으면 null")
    val nextCursor: Long?,

    @Schema(description = "과거 방향으로 더 조회할 메시지가 있는지 여부")
    val hasPrev: Boolean,

    @Schema(description = "최신 방향으로 더 조회할 메시지가 있는지 여부")
    val hasNext: Boolean
)
