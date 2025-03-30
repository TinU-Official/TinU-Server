package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.member.dto.client_controller.request.RegisterRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailAuthRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailCodeCheckRequestDTO
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.domain.member.service.RegisterService
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/register")
class RegisterController(
    val memberService: MemberService,
    val registerService: RegisterService
) {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    /*
        email-check 로직
           1. 사용 가능한 도메인인지 확인(학교 계정인지 확인)
                -> University에 없는 도메인이라면 학교 계정이 아닌 것 같다는 결과 반환
           2. 현재 Member 중에 해당 eMail을 쓰고 있는 Member가 있는지 체크
                -> 있다면 이미 사용 중인 이메일이라는 결과 반환
     */
    @GetMapping("/email-check")
    fun emailCheck(@AuthenticationPrincipal userId : UUID,@RequestParam(name = "email") email : String) : ResponseEntity<ResponseDTO<NullResponse?>> {
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
    fun emailCodeRequest(@AuthenticationPrincipal userId : UUID, @RequestBody emailAuthRequestDTO: EmailAuthRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
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
    fun emailCodeCheckRequest(@AuthenticationPrincipal userId : UUID, @RequestBody emailCodeCheckRequestDTO: EmailCodeCheckRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
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
    fun nickNameCheckRequest(@AuthenticationPrincipal userId : UUID,@RequestParam(name = "name") nickName :String) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.usableMemberByNickname(userId,nickName)
        return ResponseEntityGenerator.onSuccess()
    }

    @PostMapping("/info-verify")
    fun registerRequest(@AuthenticationPrincipal userId : UUID, @RequestBody registerRequestDTO : RegisterRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.registerMember(userId = userId, registerRequestDTO = registerRequestDTO)

        return ResponseEntityGenerator.onSuccess()
    }

}