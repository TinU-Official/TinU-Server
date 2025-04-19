package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import com.tinuproject.tinu.domain.enums.Social
import com.tinuproject.tinu.domain.member.dto.client_controller.request.UpdateUserInfoRequestDTO
import com.tinuproject.tinu.domain.member.dto.controller_service.input.UpdateUserInputDTO
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
) : BaseEntity(){

    fun updateMemberInfo(updateUserInputDTO: UpdateUserInputDTO){
        this.nickname = updateUserInputDTO.nickname
        this.grade = updateUserInputDTO.grade
        this.major = updateUserInputDTO.major
        this.introduction = updateUserInputDTO.introduction
        this.profileImageURL = updateUserInputDTO.profile
    }

}