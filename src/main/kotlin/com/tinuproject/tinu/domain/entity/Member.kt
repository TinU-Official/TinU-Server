package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import com.tinuproject.tinu.domain.enum.Gender
import com.tinuproject.tinu.domain.enum.Social
import jakarta.persistence.*

@Entity
class Member (

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

    @Column
    var mark : Double?,

    @Column
    @Enumerated(EnumType.ORDINAL)
    var social : Social,

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "author"
    )
    var post : MutableList<Post> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member")
    var scrap : MutableList<Scrap> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "buyer")
    var buyerChat : MutableList<Chat> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "seller")
    var sellerChat : MutableList<Chat> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "inquirer")
    var inquiry: MutableList<Inquiry> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member")
    var customFilter : MutableList<CustomFilter> = mutableListOf()
) : BaseEntity()