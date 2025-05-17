package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.Chat
import com.tinuproject.tinu.domain.member.entity.SocialMember
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRepository : JpaRepository<Chat, Long>, ChatSupportRepository {

    fun findByPostIdAndBuyerId(postId: Long, buyerId: Long): Chat?
    fun findChatById(chatId: Long): Chat?

    fun findChatByBuyerId(userId : Long) : Chat?
    fun findChatBySellerId(userId : Long) : Chat?
    fun existsChatByBuyerId(userId : Long) : Boolean
    fun findAllByPostId(postId : Long) : List<Chat>
    fun findChatByPostIdAndBuyerId(postId : Long, buyerId : Long) : List<Chat>
    fun findAllByBuyerIdOrSellerId(buyerId : Long, sellerId : Long) : List<Chat>
    fun findAllByBuyerId(buyerId : Long) : List<Chat>
    fun findAllBySellerId(sellerId : Long) : List<Chat>
}