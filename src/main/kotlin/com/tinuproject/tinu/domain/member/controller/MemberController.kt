package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.domain.member.controller.docs.MemberSwaggerDocs
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.domain.member.exception.NotExistMemberException
import com.tinuproject.tinu.domain.member.exception.ExistNameException
import com.tinuproject.tinu.domain.member.exception.NeedRegistException
import com.tinuproject.tinu.infra.s3.exception.InvalidETagException
import com.tinuproject.tinu.infra.s3.exception.NoSuchKeyException
import com.tinuproject.tinu.domain.member.exception.ExpiredTokenException
import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import com.tinuproject.tinu.domain.member.controller.dto.request.UpdateUserInfoRequestDTO
import com.tinuproject.tinu.domain.member.controller.dto.response.MemberSearchResponseDTO
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
) : MemberSwaggerDocs{
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping()
    override fun requestUserInfo(@AuthenticationPrincipal userId : UUID, @RequestParam(name = "userId") searchUserId : UUID? ) : ResponseEntity<ResponseDTO<MemberSearchResponseDTO?>>{
        val findUserId = searchUserId ?: userId

        return ResponseEntityGenerator.onSuccess(memberService.findMemberByUserId(
            requestUserId = userId,
            searchUserId = findUserId
        ))
    }

    @PutMapping()
    override fun requestUpdateUserInfo(@AuthenticationPrincipal userId: UUID, @RequestBody updateUserInfoRequestDTO: UpdateUserInfoRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.updateMember(userId, updateUserInfoRequestDTO)

        return ResponseEntityGenerator.onSuccess()
    }

    @GetMapping("/is-login")
    override fun requestIsLogin(@AuthenticationPrincipal userId : UUID):ResponseEntity<ResponseDTO<NullResponse?>>{
        return ResponseEntityGenerator.onSuccess()
    }
}