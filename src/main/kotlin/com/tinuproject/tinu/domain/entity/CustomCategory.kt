package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

@Entity
class CustomCategory (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id : Long ?= null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customfilter_id")
        var customFilters : CustomFilter,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        var category : Category

)
