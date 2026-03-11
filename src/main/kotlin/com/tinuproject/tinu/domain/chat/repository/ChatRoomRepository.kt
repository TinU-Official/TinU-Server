package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
}
