package com.tinuproject.tinu.domain.member.controller.docs

import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.domain.member.controller.dto.request.UpdateUserInfoRequestDTO
import com.tinuproject.tinu.domain.member.controller.dto.response.MemberSearchResponseDTO
import com.tinuproject.tinu.domain.member.exception.*
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

@Tag(name = "사용자 API", description = "사용자의 정보와 관련한 API입니다.")
interface MemberSwaggerDocs {

    @Operation(
        summary = "유저 정보 조회 API",
        description = "유저 정보 조회 API입니다." +
                "<br>자기자신에대한 정보를 조회하고 싶다면 RequestParam을 비워주시면되고" +
                "<br>다른 유저의 정보를 조회하고 싶다면 해당 User의 Id(UUID)를 RequstParam에 담아주시면 됩니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [NotExistMemberException::class]
    )
    fun requestUserInfo(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(name = "userId") searchUserId: UUID?
    ): ResponseEntity<ResponseDTO<MemberSearchResponseDTO?>>

    @Operation(
        summary = "유저 정보 갱신 API",
        description = "유저 정보를 갱신하는 API입니다." +
                "<br>필수 파라미터 : nickName" +
                "<br>선택적(nullable) 파라미터 : introduction, major, grade, profile" +
                "<br>업데이트되는 정보 + 유지되는 정보 모두 함께 보내주시면 되겠습니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [
            NotExistMemberException::class,
            ExistNameException::class,
            NoSuchKeyException::class,
            InvalidETagException::class
        ]
    )
    fun requestUpdateUserInfo(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody updateUserInfoRequestDTO: UpdateUserInfoRequestDTO
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "유저 로그인 여부 확인 API",
        description = "유저가 로그인 상태인지 확인하는 API입니다." +
                "<br>토큰의 유효성을 검사하고 회원가입 여부를 확인하여 예외를 던져줍니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [
            ExpiredTokenException::class,
            InvalidedTokenException::class,
            NotFoundTokenException::class,
            NeedRegistException::class
        ]
    )
    fun requestIsLogin(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<ResponseDTO<NullResponse?>>
}
