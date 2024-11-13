package com.tinuproject.tinu.domain.entity


import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class CustomFilter (

    @Column
    var filterName : String,

    @Column
    var maxPrice : Int?,

    @Column
    var minPrice :Int?,

    @Column
    var isSell : Boolean?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var member : Member,

    @OneToMany(mappedBy = "customFilter", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var customCategory: MutableList<CustomCategory> = mutableListOf()
) : BaseEntity()