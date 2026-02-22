package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatRoom
import com.tinuproject.tinu.domain.chat.entity.ChatRoomMember
import com.tinuproject.tinu.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomMemberRepository : JpaRepository<ChatRoomMember, Long> {
    fun findByMemberAndChatRoom(member: Member, chatRoom: ChatRoom): ChatRoomMember?
}
