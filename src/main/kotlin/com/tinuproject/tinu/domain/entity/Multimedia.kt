package com.tinuproject.tinu.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Multimedia (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    @Column
    var url : String,

    @Column
    var isImage : Boolean,

    //Entity 정리에는 연관ID라는게 있는데 뭔지 모르겠어요
)