package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.domain.member.controller.docs.RegisterSwaggerDocs
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.domain.member.exception.NeedEmailAuthException
import com.tinuproject.tinu.domain.member.exception.NotExistCodeException
import com.tinuproject.tinu.domain.member.exception.NotMatchCodeException
import com.tinuproject.tinu.domain.member.exception.ExistEmailException
import com.tinuproject.tinu.domain.member.exception.ExistMemberException
import com.tinuproject.tinu.domain.member.exception.ExistNameException
import com.tinuproject.tinu.infra.s3.exception.InvalidETagException
import com.tinuproject.tinu.infra.s3.exception.NoSuchKeyException
import com.tinuproject.tinu.domain.university.exception.NotExistDomainException
import com.tinuproject.tinu.domain.member.controller.dto.request.RegisterRequestDTO
import com.tinuproject.tinu.domain.member.controller.dto.request.EmailAuthRequestDTO
import com.tinuproject.tinu.domain.member.controller.dto.request.EmailCodeCheckRequestDTO
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.domain.member.service.RegisterService
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

@Tag( name = "회원가입 관련 API",description = "회원가입 로직 중 사용되는 API들입니다.")
@RestController
@RequestMapping("/api/register")
class RegisterController(
    val memberService: MemberService,
    val registerService: RegisterService
) : RegisterSwaggerDocs {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    /*
        email-check 로직
           1. 사용 가능한 도메인인지 확인(학교 계정인지 확인)
                -> University에 없는 도메인이라면 학교 계정이 아닌 것 같다는 결과 반환
           2. 현재 Member 중에 해당 eMail을 쓰고 있는 Member가 있는지 체크
                -> 있다면 이미 사용 중인 이메일이라는 결과 반환
     */
    @GetMapping("/email-check")
    override fun emailCheck(@AuthenticationPrincipal userId : UUID, @RequestParam(name = "email") email : String) : ResponseEntity<ResponseDTO<NullResponse?>> {
        registerService.checkEmailValidation(userId,email)

        return ResponseEntityGenerator.onSuccess()
    }


    /*
        email-auth(인증코드 전송) 로직
            1,2. email-check의 로직을 한번 실행.
            3. 현재 EMailAuth 중에 해당 eMail을 쓰고 있는 EMailAuth가 있는 지 체크
                -> 있다면 기존의 EMailAuth는 삭제하고 새로운 eMailAuth을 저장
            4. 인증코드 전송
                -> 전송했다는 결과 반환
     */
    //인증번호 요청
    @PostMapping("/email-auth")
    override fun emailCodeRequest(@AuthenticationPrincipal userId : UUID, @RequestBody emailAuthRequestDTO: EmailAuthRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        registerService.checkEmailValidation(userId,emailAuthRequestDTO.email)

        registerService.sendMail(userId,emailAuthRequestDTO)

        return ResponseEntityGenerator.onSuccess()
    }

    /*
        code-check 로직
            1. 요청을 보낸 userId의 eMailAuth 찾기
            2. 해당 eMailAuth의 code와 요청에 담긴 code가 동일한지 확인
                - 동일하다면 eMailAuth의 approve(통과) 값을 true로 설정
                - 다르다면 다르다는 Exception 발생.
            4. 성공 응답 전송
     */

    @PutMapping(("/code-check"))
    override fun emailCodeCheckRequest(@AuthenticationPrincipal userId : UUID, @RequestBody emailCodeCheckRequestDTO: EmailCodeCheckRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        registerService.checkCode(userId, emailCodeCheckRequestDTO)
        return ResponseEntityGenerator.onSuccess()
    }

    /*
        nick-check 로직
            1. 요청을 보낸 nickName의 사용 가능 여부 체크
                - nickName을 사용 중인 Member가 있는 지 확인.
                    - 이때 해당 nickName을 본인이 사용 중인 것이라면 가능하다는 응답 필요
            2. 가능하다면 가능하다는 응답 전송
     */
    @GetMapping("/nick-check")
    override fun nickNameCheckRequest(@AuthenticationPrincipal userId : UUID, @RequestParam(name = "name") nickName :String) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.usableMemberByNickname(userId,nickName)
        return ResponseEntityGenerator.onSuccess()
    }

    @PostMapping("/info-verify")
    override fun registerRequest(@AuthenticationPrincipal userId : UUID, @RequestBody registerRequestDTO : RegisterRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.registerMember(userId = userId, registerRequestDTO = registerRequestDTO)

        return ResponseEntityGenerator.onSuccess()
    }

}