package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*


@Entity
class University (

    @OneToMany(mappedBy = "university")
    var members : MutableList<Member> ?= mutableListOf(),

    @Column
    var domain : String,

    @Column
    var name : String
) : BaseEntity()