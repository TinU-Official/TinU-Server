package com.tinuproject.tinu.domain.member.entity

import com.tinuproject.tinu.global.entity.BaseEntity
import com.tinuproject.tinu.domain.member.enums.Social
import com.tinuproject.tinu.domain.member.service.dto.input.UpdateUserInputDTO
import com.tinuproject.tinu.domain.university.entity.University
import com.tinuproject.tinu.domain.chat.entity.ChatRoomMember
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

    /**
     * ChatRoom 이 buyer/seller 를 직접 참조하던 초기 설계는 608402d 에서
     * ChatRoomMember 기반 모델로 정리되었다. Member ↔ ChatRoom 양방향은
     * ChatRoomMember 컬렉션 하나로 통합한다 (이전의 buyerChatRoom/sellerChatRoom 제거).
     * 더 큰 엔티티 모델링 재검토는 별도 회의에서 다룬다.
     */
    @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        mappedBy = "member")
    var chatRoomMembers: MutableList<ChatRoomMember> = mutableListOf(),

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