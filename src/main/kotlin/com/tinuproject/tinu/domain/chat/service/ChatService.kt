package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.response.ChatListResponse
import com.tinuproject.tinu.domain.chat.dto.response.CreateChatResponse
import java.util.*

interface ChatService {

    // 메시지(stomp) 관련
    fun createRoom(userId : UUID, postId : Long) : CreateChatResponse

    fun getList(userId : UUID) : List<ChatListResponse>

    // 채팅방(http) 관련

}