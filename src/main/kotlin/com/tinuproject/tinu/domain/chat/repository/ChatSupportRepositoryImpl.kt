package com.tinuproject.tinu.domain.chat.repository
import com.querydsl.jpa.impl.JPAQueryFactory
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatListGetOutputDto
import com.tinuproject.tinu.domain.chat.entity.QChat.chat
import com.tinuproject.tinu.domain.chat.entity.QChatText.chatText
import com.tinuproject.tinu.domain.post.entity.QPost.post
import com.querydsl.core.types.Projections
import java.util.*
import org.springframework.stereotype.Repository

@Repository
class ChatSupportRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ChatSupportRepository {
    override fun findChatListByUserIdAndType(userId: Long, sortedType: String): List<ChatListGetOutputDto> {

        // ChatText의 최신 메시지만 가져오기 위한 서브쿼리
        val subQuery = queryFactory.select(chatText.id)
            .from(chatText)
            .where(chatText.chat.id.eq(chat.id))
            .orderBy(chatText.createdAt.desc())
            .limit(1)

        return queryFactory.select(
            Projections.constructor(
                ChatListGetOutputDto::class.java,
                chat.id,
                chat.post.thumbnail,
                chat.buyer.id,
                chat.seller.id,
                chat.post.id,
                chatText.text,
                chatText.createdAt
            )
        )
            .from(chat)
            .leftJoin(chat.post, post).fetchJoin()
            .leftJoin(chatText).on(chatText.id.eq(subQuery))
            .where(
                when (sortedType) {
                    "buy" -> chat.buyer.id.eq(userId)
                    "sell" -> chat.seller.id.eq(userId)
                    else -> null
                }
            )
            .fetch()
    }
}