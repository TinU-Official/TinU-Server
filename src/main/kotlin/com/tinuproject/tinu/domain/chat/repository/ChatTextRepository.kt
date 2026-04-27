package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatText
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ChatTextRepository : JpaRepository<ChatText, Long> {
    fun findByChatRoomIdAndOrder(chatRoomId: Long, order: Long): ChatText?

    // 채팅방 전체 메시지를 시간순으로 반환. Pageable로 상한 강제. writer EntityGraph로 N+1 차단.
    @EntityGraph(attributePaths = ["writer"])
    fun findAllByChatRoomIdOrderByOrderAsc(chatRoomId: Long, pageable: Pageable): List<ChatText>
}
