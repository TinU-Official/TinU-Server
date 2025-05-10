package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.GetListChatResponse
import com.tinuproject.tinu.domain.chat.controller.dto.response.CreateChatResponse
import java.util.*

interface ChatService {

    fun createRoom(userId : UUID, postId : Long) : CreateChatResponse

    fun getList(userId : UUID, sortedType : String) : List<GetListChatResponse>

//    fun getChatDetail(userId: UUID, chatId: Long): GetListChatResponse
}