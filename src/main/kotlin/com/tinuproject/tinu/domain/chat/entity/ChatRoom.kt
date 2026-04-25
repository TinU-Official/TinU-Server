package com.tinuproject.tinu.domain.chat.entity

import com.tinuproject.tinu.domain.post.entity.Post
import com.tinuproject.tinu.global.entity.BaseEntity
import jakarta.persistence.*

/**
 * 채팅방 엔티티. post 기준 1:1 채팅방.
 * @see JoinColumn(nullable = false): 필수 관계를 스키마에 반영해 null 참조 방지 및 도메인 규칙 명시
 */
@Entity
@Table(
    name = "chat_room",
    indexes = [Index(name = "idx_chat_room_post_id", columnList = "post_id")]
)
class ChatRoom(

    /** 상품. 채팅방이 어떤 상품에 대한 대화인지 식별. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post,

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "chatRoom"
    )
    var chatList: MutableList<ChatText> = mutableListOf()

) : BaseEntity()
