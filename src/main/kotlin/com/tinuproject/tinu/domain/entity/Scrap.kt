package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class Scrap (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var member : Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post : Post
) : BaseEntity()