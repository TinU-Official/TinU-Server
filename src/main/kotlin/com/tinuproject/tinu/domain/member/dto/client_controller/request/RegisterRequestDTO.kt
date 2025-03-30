package com.tinuproject.tinu.domain.member.dto.client_controller.request

data class RegisterRequestDTO(
    val nickName : String,
    val profileImageURL : String?,
    val major : String,
    val grade : Int,
    val email : String,
    val introduction : String?
)
