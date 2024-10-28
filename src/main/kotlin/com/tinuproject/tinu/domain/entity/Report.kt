package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import com.tinuproject.tinu.domain.enum.PaymentMethod
import com.tinuproject.tinu.domain.enum.ReportCategory
import jakarta.persistence.*

@Entity
class Report (

    //단방향 조회로 구성하기에 Member에 따로 추가 X
    //유저 삭제시 같이 삭제되는 로직 필요
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    var reporter : Member,


    //단방향 조회로 구성하기에 Member에 따로 추가 X
    //유저 삭제시 같이 삭제되는 로직 필요
    @ManyToOne
    @JoinColumn(name = "respondent_id")
    var respondent : Member,

    //단방향 조회로 구성하여 Post에 따로 추가 X
    //유저 삭제시 같이 삭제되는 로직 필요
    @ManyToOne
    @JoinColumn(name = "post_id")
    var post : Post?,


    @Enumerated(EnumType.STRING)
    var reportCategory : ReportCategory,


    @Column(columnDefinition = "TEXT")
    var body : String?

) : BaseEntity()