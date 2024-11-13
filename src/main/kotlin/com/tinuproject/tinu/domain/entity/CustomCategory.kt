package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class CustomCategory(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFilter_id")
    var customFilter: CustomFilter,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category
) : BaseEntity()
