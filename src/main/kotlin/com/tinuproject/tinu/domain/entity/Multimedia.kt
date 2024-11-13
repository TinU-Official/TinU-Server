package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*


@Entity
class Multimedia (

    @Column
    var url : String,

    @Column
    var isImage : Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    var post : Post
) : BaseEntity()