package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

@Entity
class PostHashTagMap (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post : Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    var hashTag : HashTag,

)