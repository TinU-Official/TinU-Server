package com.tinuproject.tinu.domain.member.dto.controller_service.input

import com.tinuproject.tinu.domain.member.dto.client_controller.request.UpdateUserInfoRequestDTO
import io.swagger.v3.oas.annotations.media.Schema

class UpdateUserInputDTO(updateUserInfoRequestDTO: UpdateUserInfoRequestDTO, url : String?){
    var nickname : String = updateUserInfoRequestDTO.nickname
    var introduction : String? = updateUserInfoRequestDTO.introduction
    var major : String? = updateUserInfoRequestDTO.major
    var grade : Int? = updateUserInfoRequestDTO.grade
    var profile : String? = url
}
