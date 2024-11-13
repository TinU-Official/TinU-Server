package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class Category (

        @Column
        var name : String,

        @Column
        var level : Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        var parent : Category?,

        @OneToMany(mappedBy = "parent", cascade = [CascadeType.REMOVE])
        var child : MutableList<Category> = mutableListOf(),

        @OneToMany(mappedBy = "category", cascade = [CascadeType.REMOVE])
        var customCategory : MutableList<CustomCategory> = mutableListOf(),

        @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
        var posts : MutableList<Post> = mutableListOf()
) : BaseEntity()