package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatRoom
import com.tinuproject.tinu.domain.member.entity.Member
import com.tinuproject.tinu.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    fun findByBuyerAndSellerAndPost(buyer: Member, seller: Member, post: Post): ChatRoom?
}
