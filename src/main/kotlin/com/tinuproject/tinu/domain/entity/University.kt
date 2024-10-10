package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*


@Entity
class University (
    //id, 학교명, 학교이메일 도메인

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,


    @OneToMany(mappedBy = "university")
    var members : MutableList<Member> ?= null,

    @Column
    var domain : String

    )