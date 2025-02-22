package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.domain.member.dto.client_controller.RegisterRequestDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
interface MemberService {

    fun registerMember(userId: UUID, registerRequestDTO: RegisterRequestDTO)

    fun usableMemberByNickName(userId : UUID, name : String) : Boolean

    fun usableMemberByEmail(userId: UUID,email : String) :Boolean
}