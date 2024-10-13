package com.tinuproject.tinu.domain.entity

import jakarta.persistence.*

@Entity
class Evaluation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?= null,

    //단방향 조회로 구성하기에 Member에 따로 추가 X
    //유저 삭제시 같이 삭제되는 로직 필요
    @ManyToOne
    @JoinColumn(name = "member_id")
    var evaluator : Member,


    //단방향 조회로 구성하기에 Member에 따로 추가 X
    //유저 삭제시 같이 삭제되는 로직 필요
    @ManyToOne
    @JoinColumn(name = "member_id")
    var evaluatee : Member,


    //평가 만족도를 어떻게 관리해야할지 모르겠음
    //해당 부분의 아이디어를 받습니다.
    @Column
    var score : Double
)