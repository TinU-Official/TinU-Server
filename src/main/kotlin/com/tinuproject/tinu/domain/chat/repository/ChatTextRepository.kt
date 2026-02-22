package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatText
import org.springframework.data.jpa.repository.JpaRepository

interface ChatTextRepository : JpaRepository<ChatText, Long> {
    fun findByChatRoomIdAndOrder(chatRoomId: Long, order: Long): ChatText?
    fun findAllByIdIn(ids: Set<Long>): List<ChatText>
}
