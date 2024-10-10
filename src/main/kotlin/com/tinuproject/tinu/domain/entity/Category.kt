package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

@Entity
class Category (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Long ?= null,

        @Column
        var name : String,

        @Column
        var level : Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        var parent : Category ?= null,

        @OneToMany(mappedBy = "parent", cascade = [CascadeType.REMOVE])
        var child : MutableList<Category> ?= null,

        @OneToMany(mappedBy = "category", cascade = [CascadeType.REMOVE])
        var customCategory : MutableList<CustomCategory>,

        @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
        var posts : MutableList<Post>
)