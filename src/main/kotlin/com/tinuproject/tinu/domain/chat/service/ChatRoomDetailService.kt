package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.request.ChatDetailRequest
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatDetailResponse
import java.util.UUID

interface ChatRoomDetailService {
    fun getChatDetail(
        userId: UUID,
        chatRoomId: Long,
        request: ChatDetailRequest
    ): ChatDetailResponse
}
