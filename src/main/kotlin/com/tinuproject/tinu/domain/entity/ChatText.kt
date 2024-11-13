package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import com.tinuproject.tinu.domain.enum.ChatRole
import com.tinuproject.tinu.domain.enum.ChatType
import jakarta.persistence.*

@Entity
class ChatText (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_id")
    var chat : Chat,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var writer : Member,

    @Column
    var text : String,

    @Column
    @Enumerated(EnumType.STRING)
    var type : ChatType,

    @Column
    var isRead : Boolean
) : BaseEntity()