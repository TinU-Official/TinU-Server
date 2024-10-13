package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.category.CustomCategoryL
import com.tinuproject.tinu.domain.entity.category.CustomCategoryM
import jakarta.persistence.*

@Entity
class CustomFilter (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,


    @Column
    var filterName : String,

    @Column
    var maxPrice : Int?,

    @Column
    var minPrice :Int?,

    @Column
    var isSell : Boolean?,
    
    //member에 추가 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var member : Member,


    @OneToMany(mappedBy = "customfilter", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var customCategoryL: MutableList<CustomCategoryL>,

    @OneToMany(mappedBy = "customfilter", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var customCategoryM: MutableList<CustomCategoryM>,

    @OneToMany(mappedBy = "customfilter", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var customCategory: MutableList<CustomCategory>


)