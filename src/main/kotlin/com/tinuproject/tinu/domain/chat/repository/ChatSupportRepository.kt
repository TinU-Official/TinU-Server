package com.tinuproject.tinu.domain.chat.repository
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatListGetOutputDto
import java.util.*

interface ChatSupportRepository {
    fun findChatListByUserIdAndType(userId: Long, sortedType: String): List<ChatListGetOutputDto>
}