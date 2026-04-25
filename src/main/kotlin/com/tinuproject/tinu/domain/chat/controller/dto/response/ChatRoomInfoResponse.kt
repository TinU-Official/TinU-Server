package com.tinuproject.tinu.domain.chat.controller.dto.response

import com.tinuproject.tinu.domain.chat.enums.ChatRole
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅방 정보 응답")
data class ChatRoomInfoResponse(
    @Schema(description = "채팅방 ID")
    val chatRoomId: Long,

    @Schema(description = "상대방 닉네임")
    val opponentNickname: String,

    @Schema(description = "상대방 프로필 이미지 URL (없을 수 있음)")
    val opponentProfileImageURL: String?,

    @Schema(description = "상대방 멤버 ID")
    val opponentMemberId: Long,

    @Schema(description = "상품 ID")
    val postId: Long,

    @Schema(description = "상품명")
    val postTitle: String,

    @Schema(description = "상품 가격")
    val postPrice: Int,

    @Schema(description = "상품 썸네일 URL (없을 수 있음)")
    val postThumbnail: String?,

    @Schema(description = "상품 판매 완료 여부 (true=판매완료)")
    val postIsSoldOut: Boolean,

    @Schema(description = "상대방이 채팅방을 나갔는지 여부")
    val opponentHasLeft: Boolean,

    @Schema(description = "내 역할 (BUYER or SELLER)")
    val myRole: ChatRole
)
