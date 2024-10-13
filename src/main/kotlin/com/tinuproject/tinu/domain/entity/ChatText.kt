package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.enum.ChatRole
import com.tinuproject.tinu.domain.enum.ChatType
import jakarta.persistence.*

@Entity
class ChatText (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,

    @Column
    var role : ChatRole,

    @Column
    var text : String,

    @Column
    @Enumerated(EnumType.STRING)
    var type : ChatType,
)