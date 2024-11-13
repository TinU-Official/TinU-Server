package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import com.tinuproject.tinu.domain.enum.PaymentMethod
import com.tinuproject.tinu.domain.enum.ReportCategory
import jakarta.persistence.*

@Entity
class Report (

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    var reporter : Member,

    @ManyToOne
    @JoinColumn(name = "respondent_id")
    var respondent : Member,

    @ManyToOne
    @JoinColumn(name = "post_id")
    var post : Post?,

    @Enumerated(EnumType.STRING)
    var reportCategory : ReportCategory,

    @Column(columnDefinition = "TEXT")
    var body : String?
) : BaseEntity()