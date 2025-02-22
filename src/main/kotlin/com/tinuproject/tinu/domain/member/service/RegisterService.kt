package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.web.email.dto.client_controller.EmailAuthRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailCodeCheckRequestDTO
import java.util.UUID

interface RegisterService {
    fun sendMail(userId : UUID,emailAuthRequestDTO: EmailAuthRequestDTO)

    fun checkCode(userId : UUID, emailCodeCheckRequestDTO : EmailCodeCheckRequestDTO) : Boolean

    fun checkEmailValidation(userId : UUID, email : String) :Boolean


}