package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class Chat (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="buyer_id")
    var buyer : Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id")
    var seller : Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    var post : Post,

    @Column
    var isDeleted : Boolean,

    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "chat")
    var chatList : MutableList<ChatText> = mutableListOf()
) : BaseEntity() {
}
