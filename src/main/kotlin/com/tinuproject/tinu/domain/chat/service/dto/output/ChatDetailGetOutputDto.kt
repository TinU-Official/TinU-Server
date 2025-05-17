package com.tinuproject.tinu.domain.chat.service.dto.output

import com.tinuproject.tinu.domain.chat.controller.dto.ChatMessageGroupDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채팅방 상세 조회 응답 DTO")
data class ChatDetailGetOutputDto(
    @Schema(description = "채팅방 ID", example = "1")
    val chatId: Long,
    @Schema(description = "구매자 ID", example = "2")
    val buyerId: Long,
    @Schema(description = "판매자 ID", example = "3")
    val sellerId: Long,
    @Schema(description = "관련 게시글 ID", example = "4")
    val postId: Long,
    @Schema(description = "날짜별 메시지 그룹")
    val messages: List<ChatMessageGroupDto>
)
