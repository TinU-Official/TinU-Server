package com.tinuproject.tinu.domain.member.dto.client_controller.request

import com.tinuproject.tinu.s3.dto.request.S3VerifiableRequest


data class RegisterRequestDTO(
    val nickName : String,
    val profile : S3VerifiableRequest?,
    val major : String?,
    val grade : Int?,
    val email : String,
    val introduction : String?,
){
}
