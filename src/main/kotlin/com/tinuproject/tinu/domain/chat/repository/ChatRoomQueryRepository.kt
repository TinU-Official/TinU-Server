package com.tinuproject.tinu.domain.chat.repository

import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.repository.dto.ChatRoomRawDto
import java.time.LocalDateTime

interface ChatRoomQueryRepository {
    /**
     * 채팅 목록 조회 (QueryDSL).
     * 정렬: 마지막 메시지 createdAt desc, chatRoomId desc
     * 커서: cursorChatRoomId + cursorLastChatAt 기반 복합 커서
     *
     * @param memberId 조회 요청 멤버 id
     * @param filter ALL / PURCHASE / SALE
     * @param cursorChatRoomId 커서 chatRoomId (null이면 첫 페이지)
     * @param cursorLastChatAt 커서 기준 마지막 메시지 시각 (null이면 첫 페이지)
     * @param size 조회 개수 (size+1을 넘겨서 다음 페이지 여부 판단)
     */
    fun findChatRoomList(
        memberId: Long,
        filter: ChatRoomFilter,
        cursorChatRoomId: Long?,
        cursorLastChatAt: LocalDateTime?,
        size: Int
    ): List<ChatRoomRawDto>
}
