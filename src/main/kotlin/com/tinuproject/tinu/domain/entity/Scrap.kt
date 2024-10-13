package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

@Entity
class Scrap (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,


    //Scrap과 관련한 요소 Member에 추가 필요 (해결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var member : Member,


    //포스트에선 누가 자신을 스크랩했는지는 알 필요가 없다고 판단, 단방향 조회로 구성
    //다른 사람 의견 구함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post : Post
)