package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.ChatDTO

interface ChatService {

    // 메시지(stomp) 관련
    fun createRoom(chatDTO: ChatDTO)

    fun getList(userId : Long?)


    // 채팅방(http) 관련
}