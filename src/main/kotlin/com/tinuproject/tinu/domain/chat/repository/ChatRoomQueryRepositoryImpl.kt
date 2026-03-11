package com.tinuproject.tinu.domain.chat.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.tinuproject.tinu.domain.chat.entity.QChatRoom
import com.tinuproject.tinu.domain.chat.entity.QChatRoomMember
import com.tinuproject.tinu.domain.chat.entity.QChatText
import com.tinuproject.tinu.domain.chat.enums.ChatRole
import com.tinuproject.tinu.domain.chat.enums.ChatRoomFilter
import com.tinuproject.tinu.domain.chat.repository.dto.ChatRoomRawDto
import com.tinuproject.tinu.domain.post.entity.QPost
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ChatRoomQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ChatRoomQueryRepository {

    private val chatRoom = QChatRoom.chatRoom
    private val post = QPost.post
    /** CRM = ChatRoomMember. 현재 사용자의 ChatRoomMember */
    private val myCRM = QChatRoomMember("myCRM")
    /** CRM = ChatRoomMember. 상대방의 ChatRoomMember (opponentHasLeft 판단용) */
    private val opponentCRM = QChatRoomMember("opponentCRM")

    override fun findChatRoomList(
        memberId: Long,
        filter: ChatRoomFilter,
        cursorChatRoomId: Long?,
        cursorLastChatAt: LocalDateTime?,
        size: Int
    ): List<ChatRoomRawDto> {
        val lastChatText = QChatText("lastChatText")
        val subChatText = QChatText("subChatText")

        val maxOrderSubquery = JPAExpressions
            .select(subChatText.order.max())
            .from(subChatText)
            .where(subChatText.chatRoom.eq(chatRoom))

        return queryFactory
            .select(
                Projections.constructor(
                    ChatRoomRawDto::class.java,
                    chatRoom.id,
                    myCRM.role,
                    post.id,
                    post.title,
                    post.thumbnail,
                    lastChatText.text,
                    lastChatText.type,
                    lastChatText.createdAt,
                    lastChatText.order,
                    myCRM.lastReadChatId,
                    opponentCRM.deletedAt
                )
            )
            .from(chatRoom)
            .join(chatRoom.post, post)
            .join(lastChatText).on(
                lastChatText.chatRoom.eq(chatRoom)
                    .and(lastChatText.order.eq(maxOrderSubquery))
            )
            .leftJoin(myCRM).on(
                myCRM.chatRoom.eq(chatRoom)
                    .and(myCRM.member.id.eq(memberId))
            )
            // 채팅방에는 판매자, 구매자 2명만 존재할 수 있음. 더 늘어날 일 없음.
            .leftJoin(opponentCRM).on(
                opponentCRM.chatRoom.eq(chatRoom)
                    .and(opponentCRM.member.id.ne(memberId))
            )
            .where(
                filterCondition(filter),
                myNotLeft(),
                cursorCondition(cursorChatRoomId, cursorLastChatAt, lastChatText)
            )
            .orderBy(lastChatText.createdAt.desc(), chatRoom.id.desc())
            .limit(size.toLong())
            .fetch()
    }

    /** 필터 조건: ALL=myCRM join으로 이미 필터링됨, PURCHASE=내가 buyer, SALE=내가 seller */
    private fun filterCondition(filter: ChatRoomFilter): BooleanExpression? =
        when (filter) {
            ChatRoomFilter.ALL -> null  // myCRM join으로 이미 memberId 필터링됨
            ChatRoomFilter.PURCHASE -> myCRM.role.eq(ChatRole.BUYER)
            ChatRoomFilter.SALE -> myCRM.role.eq(ChatRole.SELLER)
        }

    /** 나간 채팅방 제외: 내 ChatRoomMember가 없거나(마이그레이션) deletedAt이 null인 경우만 표시 */
    private fun myNotLeft(): BooleanExpression =
        myCRM.id.isNull.or(myCRM.deletedAt.isNull)

    /**
     * 복합 커서 조건.
     * 정렬 기준: (lastChatAt desc, chatRoomId desc)
     * 다음 페이지: lastChatAt < cursor 또는 (lastChatAt = cursor AND chatRoomId < cursorChatRoomId)
     */
    private fun cursorCondition(
        cursorChatRoomId: Long?,
        cursorLastChatAt: LocalDateTime?,
        lastChatText: QChatText
    ): BooleanExpression? {
        if (cursorChatRoomId == null || cursorLastChatAt == null) return null
        return lastChatText.createdAt.lt(cursorLastChatAt)
            .or(
                lastChatText.createdAt.eq(cursorLastChatAt)
                    .and(chatRoom.id.lt(cursorChatRoomId))
            )
    }
}
