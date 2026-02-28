package com.tinuproject.tinu.domain.chat.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.tinuproject.tinu.domain.chat.entity.ChatText
import com.tinuproject.tinu.domain.chat.entity.QChatText
import com.tinuproject.tinu.domain.chat.enums.ChatDetailDirection
import com.tinuproject.tinu.domain.member.entity.QMember
import org.springframework.stereotype.Repository

@Repository
class ChatTextQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ChatTextQueryRepository {

    private val chatText = QChatText.chatText
    private val writer = QMember("writer")

    override fun findByCursor(
        chatRoomId: Long,
        cursorOrder: Long?,
        direction: ChatDetailDirection,
        size: Int
    ): List<ChatText> {
        val query = queryFactory
            .selectFrom(chatText)
            .join(chatText.writer, writer).fetchJoin()
            .where(chatText.chatRoom.id.eq(chatRoomId))

        if (cursorOrder == null) {
            // cursor 없을 때 = 0번째부터 NEXT로 간주 → 최신 size개 반환 (ORDER BY chat_order DESC)
            return query
                .orderBy(chatText.order.desc())
                .limit(size.toLong())
                .fetch()
        }

        return when (direction) {
            ChatDetailDirection.PREV ->
                query
                    .where(chatText.order.lt(cursorOrder))
                    .orderBy(chatText.order.desc())
                    .limit(size.toLong())
                    .fetch()

            ChatDetailDirection.NEXT ->
                query
                    .where(chatText.order.gt(cursorOrder))
                    .orderBy(chatText.order.asc())
                    .limit(size.toLong())
                    .fetch()
        }
    }
}
