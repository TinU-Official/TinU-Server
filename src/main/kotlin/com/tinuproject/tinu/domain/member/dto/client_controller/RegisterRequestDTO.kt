package com.tinuproject.tinu.domain.member.dto.client_controller

data class RegisterRequestDTO(
    val nickName : String,
    val profileImageURL : String?,
    val major : String,
    val grade : Int,
    val gender : String,
    val eMail : String,
    val introduction : String?
)
