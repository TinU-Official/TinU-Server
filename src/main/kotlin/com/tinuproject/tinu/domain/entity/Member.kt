package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.enum.Gender
import com.tinuproject.tinu.domain.enum.Social
import jakarta.persistence.*

@Entity
class Member (

    //id, 학교, 닉네임, 학과, 학년, 성별
    // , 프사, 자기소개글, 이메일, 평과결과, 신고누적(반정규화), 소셜로그인 플랫폼 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    var university: University? = null,

    @Column
    var nickname : String,

    @Column
    var major : String,

    @Column
    var grade : Int,

    @Column
    @Enumerated(EnumType.ORDINAL)
    var gender : Gender,

    @Column
    var profileImageURL : String,

    @Column
    var introduction : String,

    @Column
    var eMail : String,

    @Column
    var reportCount : Long=0,

    //학점에 해당하는 데이터
    @Column
    var mark : Double?,

    @Column
    @Enumerated(EnumType.ORDINAL)
    var social : Social,

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member"
    )
    var evaluations: MutableList<Evaluation> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member"
    )
    var posts : MutableList<Post> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member")
    var scrap : MutableList<Scrap> = mutableListOf()
)