package com.tinuproject.tinu.domain.customfilter.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.CreateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.DeleteCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.response.SelectCustomFilter
import com.tinuproject.tinu.domain.customfilter.service.CustomFilterService
import com.tinuproject.tinu.domain.exception.common.UnauthorizedAccessException
import com.tinuproject.tinu.domain.exception.customfilter.NotExistCustomFilter
import com.tinuproject.tinu.domain.exception.mail.NotExistMemberException
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.swagger.example.SelectCustomFilterExam
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/custom-filter")
@Tag(name="커스텀 필터 API", description = "커스텀 필터와 관련한 API입니다.")
class CustomFilterController(
    private val customFilterService: CustomFilterService
) {
    val log : Logger = LoggerFactory.getLogger(this::class.java)


    @GetMapping("")
    @SwaggerExceptionResponses(exceptions = [NotExistMemberException::class])
    @Operation(summary = "특정 유저가 보유한 커스텀 필터를 조회하는 API", description = "특정 유저가 보유한 커스텀 필터 목록을 조회하는 API입니다." +
            "<br>아무것도 없다면 빈리스트를 있다면 list에 커스텀 필터 목록을 채워 전달합니다.",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    content = [
                        Content(
                            examples = [
                                ExampleObject(value = SelectCustomFilterExam.EXAMPLE_CUSTOM_FILTER_RESPONSE)
                            ]
                        )
                    ]
                )
            ]
    )
    fun requestCustomFilter(@AuthenticationPrincipal userId : UUID) : ResponseEntity<ResponseDTO<List<SelectCustomFilter>?>> {
        return ResponseEntityGenerator.onSuccess(customFilterService.getCustomFilter(userId))
    }

    @PostMapping("")
    @SwaggerExceptionResponses(exceptions = [NotExistMemberException::class])
    @Operation(summary = "커스텀 필터 생성 API", description = "커스텀 필터 생성 API입니다." +
            "<br>필수 파라미터 : filterName, category, onlySell" +
            "<br>카테고리의 경우 아무것도 없다면 빈 리스트로 보내주세요." +
            "<br>onlySell의 경우 현재 판매 중 상태인 것에대한 속성입니다. true(판매중인것만) false(판매중 + 판매완료)" +
            "<br>선택 파라미터 : maxPrice, minPrice")
    fun createCustomFilter(@AuthenticationPrincipal userId : UUID, @RequestBody createCustomFilter: CreateCustomFilter) : ResponseEntity<ResponseDTO<NullResponse?>>{
        customFilterService.createCustomFilter(userId = userId, createCustomFilter= createCustomFilter)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{filterId}")
    @SwaggerExceptionResponses(exceptions = [NotExistCustomFilter::class,UnauthorizedAccessException::class])
    @Operation(summary = "커스텀 필터 업데이트 API", description = "기존 생성되어 있던 커스텀 필터를 갱신하는 API입니다." +
            "<br>필수 파라미터 : filterId(pathVariable), filterName, category, onlySell" +
            "<br>선택 파라미터 : maxPrice, minPrice")
    fun updateCustomFilter(@AuthenticationPrincipal userId : UUID, @PathVariable(name = "filterId") filterId : Long,@RequestBody updateCustomFilter: UpdateCustomFilter) : ResponseEntity<ResponseDTO<NullResponse?>>{
        updateCustomFilter.filterId = filterId

        customFilterService.updateCustomFilter(userId, updateCustomFilter)

        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{filterId}")
    @SwaggerExceptionResponses(exceptions = [NotExistCustomFilter::class,UnauthorizedAccessException::class])
    @Operation(summary = "커스텀 필터 삭제 API", description = "커스텀 필터 삭제하는 API입니다." +
            "<br>필수 파라미터 : filterId")
    fun deleteCustomFilter(@AuthenticationPrincipal userId : UUID, @PathVariable(name = "filterId") filterId: Long) : ResponseEntity<ResponseDTO<NullResponse?>>{
        customFilterService.deleteCustomFilter(userId, DeleteCustomFilter(filterId=filterId))

        return ResponseEntityGenerator.onSuccess()
    }
}