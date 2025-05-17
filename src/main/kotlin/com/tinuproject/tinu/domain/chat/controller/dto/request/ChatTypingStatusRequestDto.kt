package com.tinuproject.tinu.domain.chat.controller.dto.request

import com.tinuproject.tinu.domain.chat.enums.ChatRole
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅 작성 상태 DTO")
data class ChatTypingStatusRequestDto(
    @Schema(description = "채팅방 ID", example = "1")
    val chatId: Long,
    @Schema(description = "상태 보낸 사람 id", example = "3")
    var senderId: Long,
    @Schema(description = "상태 보낸 사람 닉네임", example = "미미미")
    var senderName: Long,
    @Schema(description = "상태 보낸 사람 구분", example = "SELLER")
    var sendorRole: ChatRole, // SELLER OR BUYER
    @Schema(description = "작성 상태", example = "true")
    val isTyping: Boolean
)