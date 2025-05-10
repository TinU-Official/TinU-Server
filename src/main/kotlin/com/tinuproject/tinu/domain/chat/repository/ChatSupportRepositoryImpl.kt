package com.tinuproject.tinu.domain.chat.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.tinuproject.tinu.domain.chat.controller.dto.response.GetListChatResponse
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
    override fun findChatListByUserIdAndType(userId: Long, sortedType: String): List<GetListChatResponse> {
        val chatQuery = queryFactory.selectFrom(chat)
            .leftJoin(chat.chatList, chatText).fetchJoin()
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
            val lastChatText = it.chatList.maxByOrNull { text -> text.createdAt ?: LocalDateTime.MIN}
            GetListChatResponse(
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