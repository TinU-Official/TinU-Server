package com.tinuproject.tinu.domain.chat.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채팅 목록 응답")
data class ChatRoomListResponse(
    @Schema(description = "채팅방 목록")
    val chatRooms: List<ChatRoomListItemResponse>,

    @Schema(description = "조회된 채팅방 수")
    val size: Int,

    @Schema(description = "다음 페이지 커서 (형식: 'chatRoomId_order', 마지막 페이지면 null)")
    val nextCursorId: String?
)
