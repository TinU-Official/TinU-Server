package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.enum.PaymentMethod
import com.tinuproject.tinu.domain.enum.SellMethod
import jakarta.persistence.*

@Entity
class Post (
    //id, 학교(외래), 제목, 내용, 작성시간(base),
    // 카테고리(외래) 논의점,
    // 가격, 판매방식, 숨김상태,
    // 거래방식, 썸네일(반정규화), 신고누적(반정규화)
    @Id
    @GeneratedValue
    var id : Long ?= null,

    @ManyToOne
    var university : University,

    @Column
    var title : String,

    @Column(columnDefinition = "TEXT")
    var body : String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var author : Member,

    //categoryM에 ManyToOne 관련한 Post 연관 매핑 필요 (했음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    var category : Category,

    @Column
    var price : Int,

    @ElementCollection(targetClass = SellMethod::class)
    @CollectionTable(name = "sell_method", joinColumns = [JoinColumn(name = "post_id")])
    @Enumerated(EnumType.STRING)
    @Column
    var sellMethod : Set<SellMethod> = setOf(),

    @Column
    var isSell : Boolean,

    @Column
    var isHide : Boolean = false,

    @ElementCollection(targetClass = PaymentMethod::class)
    @CollectionTable(name = "payment_method", joinColumns = [JoinColumn(name = "post_id")])
    @Enumerated(EnumType.STRING)
    @Column
    var paymentMethod: Set<PaymentMethod> = setOf(),

    @Column
    var thumbnailImageURL : String?,

    @Column
    var reportCount : Long = 0,

    @Column
    var scrapCount : Long = 0,

    //단방향 매핑
    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        mappedBy = "post")
    var multimedia : MutableList<Multimedia> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "post")
    var postHashTagMap: MutableList<PostHashTagMap> = mutableListOf(),

)
