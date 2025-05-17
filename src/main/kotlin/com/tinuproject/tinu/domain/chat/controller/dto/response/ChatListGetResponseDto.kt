package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채팅 리스트 조회 응답 DTO")
data class ChatListGetResponseDto(
    @Schema(description = "채팅방 ID", example = "1")
    val id: Long,
    @Schema(description = "판매 상품 썸네일", example = "thumbnail.jpg")
    val thumbnail : String?,
    @Schema(description = "구매자 ID", example = "1")
    val buyerId: Long,
    @Schema(description = "판매자 ID", example = "2")
    val sellerId: Long,
    @Schema(description = "상품 ID", example = "1")
    val postId: Long,
    @Schema(description = "마지막 채팅 내용", example = "구매하고 싶습니다.")
    val lastChat : String?,
    @Schema(description = "마지막 채팅 시간", example = "2025-05-16T12:00:00")
    val lastTime: LocalDateTime?,
    // 안읽은 채팅 개수 필드 추가 필요
    // @Schema(description = "안읽은 채팅 개수", example = "3")
    // val unreadCount: Int?
)
