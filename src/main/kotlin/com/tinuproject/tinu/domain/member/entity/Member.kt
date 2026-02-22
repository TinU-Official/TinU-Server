package com.tinuproject.tinu.domain.member.entity

import com.tinuproject.tinu.global.entity.BaseEntity
import com.tinuproject.tinu.domain.member.enums.Social
import com.tinuproject.tinu.domain.member.service.dto.input.UpdateUserInputDTO
import com.tinuproject.tinu.domain.university.entity.University
import com.tinuproject.tinu.domain.chat.entity.ChatRoom
import com.tinuproject.tinu.domain.post.entity.Post
import com.tinuproject.tinu.domain.post.entity.Scrap
import jakarta.persistence.*
import java.util.*

@Entity
class Member (

    @Column(columnDefinition = "BINARY(16)", unique = true)
    var userId : UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    var university: University? = null,

    @Column
    var nickname : String?,

    @Column
    var major : String?,

    @Column
    var grade : Int?,

    @Column
    var profileImageURL : String?,

    @Column
    var introduction : String?,

    @Column
    var email : String?,

    @Column
    var reportCount : Long=0,

    @Column
    @Enumerated(EnumType.STRING)
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
    var buyerChatRoom: MutableList<ChatRoom> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "seller")
    var sellerChatRoom: MutableList<ChatRoom> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "inquirer")
    var inquiry: MutableList<Inquiry> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member")
    var customFilter : MutableList<CustomFilter> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST,CascadeType.REMOVE],
        mappedBy = "member")
    var reviewSummary: ReviewSummary? = null

) : BaseEntity(){

    @PrePersist
    //멤버가 생성될 때 해당 reviewSummary도 함께 생성
    fun onPrePersist() {
        if (reviewSummary == null) {
            this.reviewSummary = ReviewSummary(member = this)
        }
    }

    fun updateMemberInfo(updateUserInputDTO: UpdateUserInputDTO){
        this.nickname = updateUserInputDTO.nickname
        this.grade = updateUserInputDTO.grade
        this.major = updateUserInputDTO.major
        this.introduction = updateUserInputDTO.introduction
        this.profileImageURL = updateUserInputDTO.profile
    }

}