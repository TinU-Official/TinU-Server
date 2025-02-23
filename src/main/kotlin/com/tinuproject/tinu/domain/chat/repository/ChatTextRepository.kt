package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.entity.Chat
import com.tinuproject.tinu.domain.entity.ChatText
import org.springframework.data.jpa.repository.JpaRepository

interface ChatTextRepository : JpaRepository<ChatText, Long> {

    fun findByWriter(userId : Long) : ChatText?
}