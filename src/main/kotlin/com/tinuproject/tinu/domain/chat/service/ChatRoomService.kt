package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import java.util.UUID

interface ChatRoomService {
    fun getChatRoomList(
        userId: UUID,
        filter: ChatRoomFilter,
        cursorId: String?,
        size: Int
    ): ChatRoomListResponse
}
