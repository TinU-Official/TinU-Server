package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

@Entity
class HashTag (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,

    var tagName : String,

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "hashtag")
    var postHashTagMap:  MutableList<PostHashTagMap> = mutableListOf()



)