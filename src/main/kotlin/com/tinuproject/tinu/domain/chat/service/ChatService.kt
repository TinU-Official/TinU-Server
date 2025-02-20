package com.tinuproject.tinu.domain.chat.service

import com.tinuproject.tinu.domain.chat.dto.ChatDTO

interface ChatService {

    fun createRoom(chatDTO: ChatDTO)

    fun getList(userId : Long?)
}