package com.tinuproject.tinu.domain.member.dto.client_controller.response

import com.tinuproject.tinu.domain.entity.Member
import java.util.*

data class MemberSearchResponseDTO(
    val userId : String,
    val name : String,
    val profile : String?,
    val university : String,
    val major : String,
    val introduction : String,
    val mark : String
){
    constructor(member: Member) : this(
        userId = member.userId.toString(),
        name = member.nickname!!,
        profile = member.profileImageURL,
        university = member.university!!.name,
        major = member.major!!,
        introduction = member.introduction!!,
        mark = convertMarkToGrade(member.mark!!)
    )

    companion object {
        fun convertMarkToGrade(mark: Double): String {
                return if(0.0<=mark&&mark<0.5) "F"
                else if(0.5<=mark&&mark<1.5) "D"
                else if(1.5<=mark&&mark<2.5) "C"
                else if(2.5<=mark&&mark<3.0) "B"
                else if(3.0<=mark&&mark<3.5) "B+"
                else if(3.5<=mark&&mark<4.0) "A"
                else if(4.0<=mark&&mark<4.5) "A+"
                else "U"
        }
    }
}
