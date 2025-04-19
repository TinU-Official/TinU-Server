package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.mail.NeedEmailAuthException
import com.tinuproject.tinu.domain.exception.mail.NotExistCodeException
import com.tinuproject.tinu.domain.exception.mail.NotMatchCodeException
import com.tinuproject.tinu.domain.exception.member.ExistEmailException
import com.tinuproject.tinu.domain.exception.member.ExistMemberException
import com.tinuproject.tinu.domain.exception.member.ExistNameException
import com.tinuproject.tinu.domain.exception.s3.InvalidETagException
import com.tinuproject.tinu.domain.exception.s3.NoSuchKeyException
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.member.dto.client_controller.request.RegisterRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailAuthRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailCodeCheckRequestDTO
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.domain.member.service.RegisterService
import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
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
    @SwaggerExceptionResponses(exceptions = [NotExistDomainException::class, ExistEmailException::class, ExistMemberException::class])
    @Operation(summary = "사용가능한 email인지 확인하는 API", description = "해당 이메일이 사용가능한 이메일인지 체크합니다." +
            "<br>체크 여부 : 1. 우리 서비스에서 관리하는 도메인인지 2. 이미 인증을 진행한 계정은 아닌지 3. 이미 회원가입을 완료한 회원이 아닌지 " +
            "<br>리퀘스트 파람 : 확인하고자하는 email")
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
    @SwaggerExceptionResponses(exceptions = [NotExistDomainException::class, ExistEmailException::class, ExistMemberException::class])
    @Operation(summary = "사용가능한 email에 대해 인증 메일을 보내는 API", description = "이메일을 전송하는 API입니다." +
            "<br>이메일 전송 전 해당 이메일이 유효한지 한번 더 검증합니다." +
            "<br>RequestBody로 email을 받습니다.")
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
    @SwaggerExceptionResponses(exceptions = [NotExistCodeException::class, NotMatchCodeException::class])
    @Operation(summary = "이메일 인증 코드를 확인하는 API", description = "이메일 인증 코드를 검증하는 API입니다." +
            "<br>검증 내용은 1. 이메일 인증 시도를 한 사람인 지 2. 인증 코드가 일치하는 지를 확인합니다.")
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
    @SwaggerExceptionResponses(exceptions = [ExistNameException::class])
    @Operation(summary = "사용가능한 닉네임인지 검증하는 API", description = "사용가능한 닉네임인지 검증하는 API입니다." +
            "<br>본래 사용하던 사람이 동일한 닉네임으로 검증을 진행하면 이는 가능하게 설정해두었습니다.")
    fun nickNameCheckRequest(@AuthenticationPrincipal userId : UUID,@RequestParam(name = "name") nickName :String) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.usableMemberByNickname(userId,nickName)
        return ResponseEntityGenerator.onSuccess()
    }

    @PostMapping("/info-verify")
    @SwaggerExceptionResponses(exceptions = [ExistNameException::class, ExistMemberException::class, NeedEmailAuthException::class, NotExistDomainException::class,
        NoSuchKeyException::class, InvalidETagException::class])
    @Operation(summary = "회원가입 요청 API", description = "모든 회원가입이 과정이 끝나고 실제 회원을 등록할 때 사용하는 API입니다." +
            "<br>앞선 과정에서 인증한 정보도 한번 더 검증하는 과정을 거칩니다." +
            "<br>필수 파라미터 : nickName, eMail" +
            "<br>선택적 파라미터 : profile, major, grade, introduction" +
            "<br>major의 경우 별다른 입력이 없었다면(RequestBody에 포함되지 않았다면) 중고거래학과로 자동 할당하게 해두었습니다.")
    fun registerRequest(@AuthenticationPrincipal userId : UUID, @RequestBody registerRequestDTO : RegisterRequestDTO) : ResponseEntity<ResponseDTO<NullResponse?>>{
        memberService.registerMember(userId = userId, registerRequestDTO = registerRequestDTO)

        return ResponseEntityGenerator.onSuccess()
    }

}