package com.tinuproject.tinu.domain.chat.controller.dto.response

import com.tinuproject.tinu.domain.chat.enums.ChatRole
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채팅방 목록 항목")
data class ChatRoomListItemResponse(
    @Schema(description = "채팅방 ID")
    val chatRoomId: Long,

    @Schema(description = "상품 ID")
    val postId: Long,

    @Schema(description = "상품 썸네일 URL (없을 수 있음)")
    val postThumbnail: String?,

    @Schema(description = "상품명")
    val postTitle: String,

    @Schema(description = "마지막 채팅 내용 (이미지이면 '[사진]')")
    val lastChatContent: String,

    @Schema(description = "마지막 채팅 시각")
    val lastChatAt: LocalDateTime,

    @Schema(description = "안 읽은 메시지 수")
    val unreadCount: Long,

    @Schema(description = "내 역할 (BUYER or SELLER)")
    val myRole: ChatRole,

    @Schema(description = "상대방이 채팅방을 나갔는지 여부")
    val opponentHasLeft: Boolean,

    @Schema(description = "내가 마지막으로 읽은 ChatText.id (상세 조회 cursor로 사용). null이면 아직 읽지 않음")
    val lastReadChatId: Long?
)
