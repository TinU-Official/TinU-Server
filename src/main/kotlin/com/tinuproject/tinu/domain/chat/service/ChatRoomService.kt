package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.request.CreateChatRoomRequest
import com.tinuproject.tinu.domain.chat.controller.dto.request.MarkAsReadRequest
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomInfoResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomListResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.CreateChatRoomResponse
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import java.util.UUID

interface ChatRoomService {
    fun getChatRoomList(
        userId: UUID,
        filter: ChatRoomFilter,
        cursorId: String?,
        size: Int
    ): ChatRoomListResponse

    fun createOrEnterChatRoom(
        userId: UUID,
        request: CreateChatRoomRequest
    ): CreateChatRoomResponse

    fun getChatRoomInfo(
        userId: UUID,
        chatRoomId: Long
    ): ChatRoomInfoResponse

    fun markAsRead(
        userId: UUID,
        chatRoomId: Long,
        request: MarkAsReadRequest
    )

    fun leaveChatRoom(
        userId: UUID,
        chatRoomId: Long
    )
}
