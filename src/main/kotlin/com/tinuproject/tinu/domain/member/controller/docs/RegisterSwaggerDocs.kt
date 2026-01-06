package com.tinuproject.tinu.domain.member.controller.docs

import com.tinuproject.tinu.domain.member.controller.dto.request.EmailAuthRequestDTO
import com.tinuproject.tinu.domain.member.controller.dto.request.EmailCodeCheckRequestDTO
import com.tinuproject.tinu.domain.member.controller.dto.request.RegisterRequestDTO
import com.tinuproject.tinu.domain.member.exception.*
import com.tinuproject.tinu.domain.university.exception.NotExistDomainException
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.infra.s3.exception.InvalidETagException
import com.tinuproject.tinu.infra.s3.exception.NoSuchKeyException
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Tag(name = "회원가입 관련 API", description = "회원가입 로직 중 사용되는 API들입니다.")
interface RegisterSwaggerDocs {

    @Operation(
        summary = "사용가능한 email인지 확인하는 API",
        description = "해당 이메일이 사용가능한 이메일인지 체크합니다." +
                "<br>체크 여부 : 1. 우리 서비스에서 관리하는 도메인인지" +
                " 2. 이미 인증을 진행한 계정은 아닌지" +
                " 3. 이미 회원가입을 완료한 회원이 아닌지" +
                "<br>리퀘스트 파람 : 확인하고자하는 email"
    )
    @SwaggerExceptionResponses(
        exceptions = [
            NotExistDomainException::class,
            ExistEmailException::class,
            ExistMemberException::class
        ]
    )
    fun emailCheck(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(name = "email") email: String
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "사용가능한 email에 대해 인증 메일을 보내는 API",
        description = "이메일을 전송하는 API입니다." +
                "<br>이메일 전송 전 해당 이메일이 유효한지 한번 더 검증합니다." +
                "<br>RequestBody로 email을 받습니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [
            NotExistDomainException::class,
            ExistEmailException::class,
            ExistMemberException::class
        ]
    )
    fun emailCodeRequest(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody emailAuthRequestDTO: EmailAuthRequestDTO
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "이메일 인증 코드를 확인하는 API",
        description = "이메일 인증 코드를 검증하는 API입니다." +
                "<br>검증 내용은 1. 이메일 인증 시도를 한 사람인지" +
                " 2. 인증 코드가 일치하는지를 확인합니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [
            NotExistCodeException::class,
            NotMatchCodeException::class
        ]
    )
    fun emailCodeCheckRequest(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody emailCodeCheckRequestDTO: EmailCodeCheckRequestDTO
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "사용가능한 닉네임인지 검증하는 API",
        description = "사용가능한 닉네임인지 검증하는 API입니다." +
                "<br>본래 사용하던 사람이 동일한 닉네임으로 검증을 진행하면 가능하도록 처리되어 있습니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [ExistNameException::class]
    )
    fun nickNameCheckRequest(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(name = "name") nickName: String
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "회원가입 요청 API",
        description = "모든 회원가입 과정이 끝난 뒤 실제 회원을 등록하는 API입니다." +
                "<br>앞선 과정에서 인증한 정보도 한번 더 검증합니다." +
                "<br>필수 파라미터 : nickName, eMail" +
                "<br>선택 파라미터 : profile, major, grade, introduction"
    )
    @SwaggerExceptionResponses(
        exceptions = [
            ExistNameException::class,
            ExistMemberException::class,
            NeedEmailAuthException::class,
            NotExistDomainException::class,
            NoSuchKeyException::class,
            InvalidETagException::class
        ]
    )
    fun registerRequest(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody registerRequestDTO: RegisterRequestDTO
    ): ResponseEntity<ResponseDTO<NullResponse?>>
}
