package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.entity.Chat
import com.tinuproject.tinu.domain.entity.SocialMember
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRepository : JpaRepository<Chat, Long> {

    fun findChatByBuyerId(userId : Long) : Chat?
    fun findChatBySellerId(userId : Long) : Chat?
    fun existsChatByBuyerId(userId : Long) : Boolean
    fun findAllByPostId(postId : Long) : List<Chat>
}