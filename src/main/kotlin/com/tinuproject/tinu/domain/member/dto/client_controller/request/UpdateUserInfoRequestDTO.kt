package com.tinuproject.tinu.domain.member.dto.client_controller.request

data class UpdateUserInfoRequestDTO(
    val nickname : String,
    val introduction : String?,
    val major : String?,
    val grade : Int
) {
}