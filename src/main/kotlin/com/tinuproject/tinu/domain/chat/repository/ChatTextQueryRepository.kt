package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.entity.ChatText
import com.tinuproject.tinu.domain.chat.enums.ChatDetailDirection

interface ChatTextQueryRepository {
    /**
     * 커서 기반 ChatText 목록 조회.
     *
     * @param chatRoomId 채팅방 id
     * @param cursorOrder 기준 chat_order. null이면 cursor 없음 → NEXT 방향으로 최신 size개 반환
     * @param direction PREV(과거) / NEXT(최신). cursorOrder가 null이면 무시
     * @param size 조회 개수 (hasNext/hasPrev 판단용으로 size+1을 넘겨서 호출)
     * @return chat_order 기준 정렬된 ChatText 목록 (writer fetch join 포함)
     */
    fun findByCursor(
        chatRoomId: Long,
        cursorOrder: Long?,
        direction: ChatDetailDirection,
        size: Int
    ): List<ChatText>
}
