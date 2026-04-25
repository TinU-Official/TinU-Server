package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailResponse
import java.util.UUID

interface ChatRoomDetailService {
    fun getChatRoomDetail(
        userId: UUID,
        chatRoomId: Long
    ): ChatDetailResponse
}
