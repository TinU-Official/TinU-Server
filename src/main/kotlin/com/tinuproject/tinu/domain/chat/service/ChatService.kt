package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.controller.dto.response.ChatRoomCreateResponseDto
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatListGetOutputDto
import java.util.*

interface ChatService {

    fun createChatRoom(userId : UUID, postId : Long) : ChatRoomCreateResponseDto

    fun getChatList(userId : UUID, sortedType : String) : List<ChatListGetOutputDto>

//    fun getChatDetail(userId: UUID, chatId: Long): GetListChatResponse
}