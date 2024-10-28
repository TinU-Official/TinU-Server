package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class Chat (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="buyer_id")
    var buyer : Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id")
    var seller : Member,

    //단방향 매핑 - post는 채팅을 알고 있을 필요가 없을 것으로 생각
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    var post : Post,

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "chat")
    var chatList : MutableList<ChatText> = mutableListOf()
) : BaseEntity()
