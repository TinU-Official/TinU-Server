package com.tinuproject.tinu.domain.chat.entity

import com.tinuproject.tinu.global.entity.BaseEntity
import com.tinuproject.tinu.domain.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 채팅방 참여 멤버 (buyer 또는 seller 1인).
 * 첫 메시지 전송 시 ChatRoom + buyer/seller 양쪽 ChatRoomMember 동시 생성.
 * @see JoinColumn(nullable = false): member, chatRoom은 필수. 스키마에 도메인 규칙 반영.
 * @see Index uk_chat_room_member: (member_id, chat_room_id) unique
 *      - 멤버별 채팅방 1개 보장, 내 채팅방 목록 필터(buyer = me OR seller = me)
 */
@Entity
@Table(
    name = "chat_room_member",
    indexes = [Index(name = "uk_chat_room_member", columnList = "member_id,chat_room_id", unique = true)]
)
class ChatRoomMember(

    /** 참여 멤버. (member, chatRoom) unique로 멤버당 1개. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    /** 소속 채팅방. ChatRoomMember는 반드시 하나의 채팅방에 속함. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    var chatRoom: ChatRoom,

    /**
     * 마지막으로 읽은 ChatText의 id (커서).
     * null = 아직 한 번도 읽지 않음 → unreadCount = 전체 메시지 수
     * 안 읽은 개수 계산: 해당 ChatText.order를 조회 후 (maxOrder - lastReadOrder)
     * 웹소켓 이벤트로 업데이트 예정.
     */
    @Column
    var lastReadChatId: Long? = null,

    /**
     * Soft Delete - 채팅방 나가기 시점.
     * null이 아니면 이 멤버가 채팅방을 나간 상태.
     * - 나간 멤버: 목록에서 해당 채팅방 미노출
     * - 상대방: opponentHasLeft = true, 채팅 전송 불가 (상세 조회에서 활용)
     */
    @Column
    var deletedAt: LocalDateTime? = null

) : BaseEntity()
