package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatRoom
import com.tinuproject.tinu.domain.chat.entity.ChatRoomMember
import com.tinuproject.tinu.domain.chat.enums.ChatRole
import com.tinuproject.tinu.domain.member.entity.Member
import com.tinuproject.tinu.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomMemberRepository : JpaRepository<ChatRoomMember, Long> {
    fun findByMemberAndChatRoom(member: Member, chatRoom: ChatRoom): ChatRoomMember?

    // 특정 post에 대해 memberId가 role로 참여한 ChatRoomMember 조회 (기존 채팅방 탐색용)
    fun findByMemberAndChatRoom_PostAndRole(member: Member, post: Post, role: ChatRole): ChatRoomMember?

    // chatRoom 내 특정 role의 ChatRoomMember 조회 (상대방 조회용)
    fun findByChatRoomAndRole(chatRoom: ChatRoom, role: ChatRole): ChatRoomMember?
}
