package com.tinuproject.tinu.domain.chat.controller.dto.response

import com.tinuproject.tinu.domain.chat.enums.ChatRole
import com.tinuproject.tinu.domain.chat.enums.ChatType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채팅 메시지 요청 DTO")
data class ChatMessageResponseDto(
    @Schema(description = "채팅 메시지 ID", example = "1")
    var chatId: Long,
    @Schema(description = "채팅 메시지 타입", example = "TEXT")
    var type: ChatType, // 텍스트 OR 이미지 OR 상태
    @Schema(description = "채팅 메시지 내용", example = "안녕하세요")
    var content: String?,
    @Schema(description = "채팅 보낸 사람 id", example = "3")
    var sender: Long,
    @Schema(description = "채팅 보낸 사람 닉네임", example = "미미미")
    var senderName: Long,
    @Schema(description = "채팅 보낸 사람 구분", example = "SELLER")
    var sendorRole: ChatRole, // SELLER OR BUYER
    @Schema(description = "채팅 보낸 시간", example = "2025-05-16T12:00:00")
    var timestamp: LocalDateTime,
)
