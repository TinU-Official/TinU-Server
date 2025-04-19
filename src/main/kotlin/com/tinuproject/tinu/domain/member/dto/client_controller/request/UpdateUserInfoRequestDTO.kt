package com.tinuproject.tinu.domain.member.dto.client_controller.request

import com.tinuproject.tinu.s3.dto.request.S3VerifiableRequest
import io.swagger.v3.oas.annotations.media.Schema


@Schema(name = "유저정보 갱신 RequestBody")
data class UpdateUserInfoRequestDTO(
    @Schema(description = "갱신할 닉네임 혹은 기존 닉네임.<br>필수 파라미터", example = "TestUser")
    val nickname : String,
    @Schema(description = "갱신할 자기소개 혹은 기존 자기소개<br>선택적 파라미터", example = "안녕하세요! 정직한 거래를 지향합니다!")
    val introduction : String?,
    @Schema(description = "갱신할 전공 혹은 기존 전공<br>선택적 파라미터", example = "중고거래학과")
    val major : String?,
    @Schema(description = "갱신할 학년 혹은 기존 학년<br>선택적 파라미터", example = "4")
    val grade : Int?,
    val profile : S3VerifiableRequest?
)