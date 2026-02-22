package com.tinuproject.tinu.domain.chat.entity

import com.tinuproject.tinu.domain.post.entity.Post
import com.tinuproject.tinu.global.entity.BaseEntity
import com.tinuproject.tinu.domain.member.entity.Member
import jakarta.persistence.*

/**
 * 채팅방 엔티티. buyer-seller-post 간 1:1 채팅방.
 * @see Index uk_chat_room_buyer_seller_post: (buyer_id, seller_id, post_id) unique
 *      - 채팅방 중복 방지, findOrCreate 시 사용
 * @see JoinColumn(nullable = false): 필수 관계를 스키마에 반영해 null 참조 방지 및 도메인 규칙 명시
 */
@Entity
@Table(
    name = "chat_room",
    indexes = [Index(name = "uk_chat_room_buyer_seller_post", columnList = "buyer_id,seller_id,post_id", unique = true)]
)
class ChatRoom(

    /** 구매자. 채팅방은 buyer-seller-post 조합으로 유일. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    var buyer: Member,

    /** 판매자. Post.author와 일치해야 함. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: Member,

    /** 상품. 채팅방이 어떤 상품에 대한 대화인지 식별. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post,

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "chatRoom"
    )
    var chatList: MutableList<ChatText> = mutableListOf()

) : BaseEntity()
