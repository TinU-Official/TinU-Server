package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class PostHashTagMap (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post : Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashTag_id")
    var hashTag : HashTag
) : BaseEntity()