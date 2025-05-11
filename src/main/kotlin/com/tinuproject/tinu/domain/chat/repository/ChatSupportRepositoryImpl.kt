package com.tinuproject.tinu.domain.chat.repository
import com.querydsl.jpa.impl.JPAQueryFactory
import com.tinuproject.tinu.domain.chat.service.dto.output.ChatListGetOutputDto
import com.tinuproject.tinu.domain.chat.entity.QChat.chat
import com.tinuproject.tinu.domain.chat.entity.QChatText.chatText
import com.tinuproject.tinu.domain.post.entity.QPost.post
import java.util.*
import java.time.LocalDateTime
import org.springframework.stereotype.Repository

@Repository
class ChatSupportRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ChatSupportRepository {
    override fun findChatListByUserIdAndType(userId: Long, sortedType: String): List<ChatListGetOutputDto> {
        val chatQuery = queryFactory.selectFrom(chat)
            .leftJoin(chat.post, post).fetchJoin()
            .where(
                when (sortedType) {
                    "buy" -> chat.buyer.id.eq(userId)
                    "sell" -> chat.seller.id.eq(userId)
                    else -> null
                }
            )
            .fetch()

        return chatQuery.map {
            // createdAt가 가장 최근인 채팅 메시지 1개만 가져오기
            val lastChatText = queryFactory.selectFrom(chatText)
                .where(chatText.chat.id.eq(it.id))
                .orderBy(chatText.createdAt.desc())
                .limit(1)
                .fetchOne()

            ChatListGetOutputDto(
                id = it.id!!,
                thumbnail = it.post.thumbnail,
                buyerId = it.buyer.id!!,
                sellerId = it.seller.id!!,
                postId = it.post.id!!,
                lastChat = lastChatText?.text,
                lastTime = lastChatText?.createdAt
            )
        }
    }
}