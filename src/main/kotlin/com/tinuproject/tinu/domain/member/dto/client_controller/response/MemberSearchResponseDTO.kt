package com.tinuproject.tinu.domain.member.dto.client_controller.response

import com.tinuproject.tinu.domain.entity.Member
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*


@Schema(description = "멤버 조회 결과")
data class MemberSearchResponseDTO(
    @Schema(description = "UUID로 구성된 UserId 입니다.", defaultValue = "UUID")
    val userId : String,

    @Schema(description = "닉네임", defaultValue = "컴공제일")
    val name : String,

    @Schema(description = "s3 Bucket에서 저장 가능한 Image Url", defaultValue = "URL")
    val profile : String?,

    @Schema(description = "소속 학교", defaultValue = "경기대학교")
    val university : String,

    @Schema(description = "소속 학과", defaultValue = "중고거래할인학과")
    val major : String,

    @Schema(description = "자기소개", defaultValue = "컴퓨터 공학 관련 거래를 자주 합니다!")
    val introduction : String,
    @Schema(description = "학점(거래 평가 점수)", defaultValue = "A+")
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
