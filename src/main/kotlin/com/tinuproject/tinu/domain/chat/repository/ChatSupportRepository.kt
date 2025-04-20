package com.tinuproject.tinu.domain.chat.repository
import com.tinuproject.tinu.domain.chat.dto.response.GetListChatResponse
import java.util.*

interface ChatSupportRepository {
    fun findChatListByUserIdAndType(userId: Long, sortedType: String): List<GetListChatResponse>
}