package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.domain.member.dto.client_controller.response.MemberSearchResponseDTO
import com.tinuproject.tinu.domain.member.dto.client_controller.request.RegisterRequestDTO
import com.tinuproject.tinu.domain.member.dto.client_controller.request.UpdateUserInfoRequestDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
interface MemberService {

    fun registerMember(userId: UUID, registerRequestDTO: RegisterRequestDTO)

    fun usableMemberByNickname(userId : UUID, nickName : String) : Boolean

    fun usableMemberByEmail(userId: UUID,email : String) :Boolean

    fun findMemberByUserId(userId: UUID) : MemberSearchResponseDTO

    fun updateMember(userId: UUID, updateUserInfoRequestDTO: UpdateUserInfoRequestDTO)
}