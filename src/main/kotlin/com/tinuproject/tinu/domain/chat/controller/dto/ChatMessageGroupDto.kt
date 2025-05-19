package com.tinuproject.tinu.domain.chat.controller.dto

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailMessageResponseDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "채팅 메시지 그룹화 DTO")
data class ChatMessageGroupDto(
    @Schema(description = "채팅 메시지 날짜 그룹 시간", example = "2025-05-16")
    val date: LocalDate,
    @Schema(description = "채팅 메시지 리스트")
    val messages: List<ChatDetailMessageResponseDto>
)