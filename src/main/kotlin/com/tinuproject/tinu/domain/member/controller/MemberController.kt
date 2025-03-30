package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.member.ExistMemberException
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.member.dto.client_controller.request.UpdateUserInfoRequestDTO
import com.tinuproject.tinu.domain.member.dto.client_controller.response.MemberSearchResponseDTO
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/user")
class MemberController(
    val memberService: MemberService,
) {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping()
    fun requestUserInfo(@AuthenticationPrincipal userId : UUID, @RequestParam(name = "userId") searchUserId : String? ) : ResponseEntity<ResponseDTO<MemberSearchResponseDTO?>>{
        val findUserId = searchUserId?.let{UUID.fromString(it)}?:userId

        return ResponseEntityGenerator.onSuccess(memberService.findMemberByUserId(findUserId))
    }

    @PutMapping()
    fun requestUpdateUserInfo(@AuthenticationPrincipal userId: UUID, @RequestBody updateUserInfoRequestDTO: UpdateUserInfoRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.updateMember(userId, updateUserInfoRequestDTO)

        return ResponseEntityGenerator.onSuccess()
    }

    @GetMapping("/is-login")
    fun requestIsLogin(@AuthenticationPrincipal userId : UUID):ResponseEntity<ResponseDTO<NullResponse?>>{
        return ResponseEntityGenerator.onSuccess()
    }
}