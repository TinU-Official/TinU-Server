package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

//문의 Entity
@Entity
class Inquiry (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var inquirer : Member,

    @Column
    var title : String,

    @Column
    var body : String,

    @Column
    var answer : String?,

    @Column
    var isAnswer : Boolean
) : BaseEntity()
