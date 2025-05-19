package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.Chat
import com.tinuproject.tinu.domain.member.entity.SocialMember
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRepository : JpaRepository<Chat, Long>, ChatSupportRepository {
    fun findByPostIdAndBuyerId(postId: Long, buyerId: Long): Chat?
    fun findChatById(chatId: Long): Chat?
}