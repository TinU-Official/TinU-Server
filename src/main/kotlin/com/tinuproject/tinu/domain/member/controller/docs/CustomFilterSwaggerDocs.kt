package com.tinuproject.tinu.domain.member.controller.docs

import com.tinuproject.tinu.domain.member.controller.dto.request.CreateCustomFilter
import com.tinuproject.tinu.domain.member.controller.dto.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.member.controller.dto.response.SelectCustomFilter
import com.tinuproject.tinu.domain.member.exception.NotExistCustomFilter
import com.tinuproject.tinu.domain.member.exception.NotExistMemberException
import com.tinuproject.tinu.global.exception.ForbiddenException
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.infra.swagger.example.SelectCustomFilterExam
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@Tag(name = "커스텀 필터 API", description = "커스텀 필터와 관련한 API입니다.")
interface CustomFilterSwaggerDocs {

    @Operation(
        summary = "특정 유저가 보유한 커스텀 필터를 조회하는 API",
        description = "특정 유저가 보유한 커스텀 필터 목록을 조회하는 API입니다." +
                "<br>아무것도 없다면 빈리스트를 있다면 list에 커스텀 필터 목록을 채워 전달합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        examples = [
                            ExampleObject(
                                value = SelectCustomFilterExam.EXAMPLE_CUSTOM_FILTER_RESPONSE
                            )
                        ]
                    )
                ]
            )
        ]
    )
    @SwaggerExceptionResponses(
        exceptions = [NotExistMemberException::class]
    )
    fun requestCustomFilter(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<ResponseDTO<List<SelectCustomFilter>?>>

    @Operation(
        summary = "커스텀 필터 생성 API",
        description = "커스텀 필터 생성 API입니다." +
                "<br>필수 파라미터 : filterName, category, onlySell" +
                "<br>카테고리의 경우 아무것도 없다면 빈 리스트로 보내주세요." +
                "<br>onlySell의 경우 현재 판매 중 상태인 것에대한 속성입니다. true(판매중인것만) false(판매중 + 판매완료)" +
                "<br>선택 파라미터 : maxPrice, minPrice"
    )
    @SwaggerExceptionResponses(
        exceptions = [NotExistMemberException::class]
    )
    fun createCustomFilter(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody createCustomFilter: CreateCustomFilter
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "커스텀 필터 업데이트 API",
        description = "기존 생성되어 있던 커스텀 필터를 갱신하는 API입니다." +
                "<br>필수 파라미터 : filterId(pathVariable), filterName, category, onlySell" +
                "<br>선택 파라미터 : maxPrice, minPrice"
    )
    @SwaggerExceptionResponses(
        exceptions = [NotExistCustomFilter::class, ForbiddenException::class]
    )
    fun updateCustomFilter(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable(name = "filterId") filterId: Long,
        @RequestBody updateCustomFilter: UpdateCustomFilter
    ): ResponseEntity<ResponseDTO<NullResponse?>>

    @Operation(
        summary = "커스텀 필터 삭제 API",
        description = "커스텀 필터 삭제하는 API입니다." +
                "<br>필수 파라미터 : filterId"
    )
    @SwaggerExceptionResponses(
        exceptions = [NotExistCustomFilter::class, ForbiddenException::class]
    )
    fun deleteCustomFilter(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable(name = "filterId") filterId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>>
}
