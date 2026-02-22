package com.tinuproject.tinu.domain.chat.entity

import com.tinuproject.tinu.global.entity.BaseEntity
import com.tinuproject.tinu.domain.chat.enums.ChatType
import com.tinuproject.tinu.domain.member.entity.Member
import jakarta.persistence.*

/**
 * @see JoinColumn(nullable = false): chatRoom, writer는 필수. 스키마에 도메인 규칙 반영.
 * @see Index uk_chat_text_room_order: (chat_room_id, chat_order) unique
 *      - 마지막 메시지 조회(chat_order desc), 안 읽은 개수(chat_order > lastReadOrder),
 *        목록 페이지네이션 커서(findByChatRoomIdAndOrder)
 */
@Entity
@Table(
    name = "chat_text",
    indexes = [Index(name = "uk_chat_text_room_order", columnList = "chat_room_id,chat_order", unique = true)]
)
class ChatText(

    /** 소속 채팅방. 메시지는 반드시 하나의 채팅방에 속함. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    var chatRoom: ChatRoom,

    /** 작성자. 메시지 전송 시 buyer 또는 seller여야 함(별도 검증). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var writer: Member,

    @Column
    var text: String,

    @Column
    @Enumerated(EnumType.STRING)
    var type: ChatType,

    /**
     * 채팅방 내 순차 번호 (1부터 시작).
     * - 안 읽은 개수: maxOrder - lastReadOrder (lastReadOrder=0이면 전체 미읽음)
     * - 목록 커서: (chatRoomId, order) 조합으로 uniquely 식별
     * - 컬럼명 chat_order: SQL 예약어 order 충돌 방지
     */
    @Column(name = "chat_order")
    var order: Long

) : BaseEntity()
