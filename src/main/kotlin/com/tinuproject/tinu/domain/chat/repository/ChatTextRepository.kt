package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.Chat
import com.tinuproject.tinu.domain.chat.entity.ChatText
import org.springframework.data.jpa.repository.JpaRepository

interface ChatTextRepository : JpaRepository<ChatText, Long> {
//    fun findByWriterId(userId : Long) : ChatText?
}