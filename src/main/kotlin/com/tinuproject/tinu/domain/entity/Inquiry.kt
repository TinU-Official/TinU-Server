package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

//문의 Entity
@Entity
class Inquiry (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?=null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var inquirer : Member,

    @Column
    var title : String,

    @Column
    var body : String
)
