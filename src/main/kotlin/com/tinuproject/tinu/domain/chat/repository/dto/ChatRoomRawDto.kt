package com.tinuproject.tinu.domain.chat.repository.dto

import com.tinuproject.tinu.domain.chat.enums.ChatRole
import com.tinuproject.tinu.domain.chat.enums.ChatType
import java.time.LocalDateTime

data class ChatRoomRawDto(
    val chatRoomId: Long,
    val myRole: ChatRole,
    val postId: Long,
    val postTitle: String,
    val postThumbnail: String?,
    val lastChatText: String,
    val lastChatType: ChatType,
    val lastChatAt: LocalDateTime,
    /** 채팅방 내 마지막 메시지 order. unreadCount = maxOrder - lastReadOrder */
    val maxOrder: Long,
    /** 내 ChatRoomMember.lastReadChatId. null이면 아직 읽지 않음 */
    val lastReadChatId: Long?,
    /** 상대방 ChatRoomMember.deletedAt. null이 아니면 상대방이 나간 상태 */
    val opponentDeletedAt: LocalDateTime?
)
