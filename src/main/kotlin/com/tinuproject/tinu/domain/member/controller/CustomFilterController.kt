package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.domain.member.controller.docs.CustomFilterSwaggerDocs
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.domain.member.controller.dto.request.CreateCustomFilter
import com.tinuproject.tinu.domain.member.controller.dto.request.DeleteCustomFilter
import com.tinuproject.tinu.domain.member.controller.dto.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.member.controller.dto.response.SelectCustomFilter
import com.tinuproject.tinu.domain.member.service.CustomFilterService
import com.tinuproject.tinu.global.exception.ForbiddenException
import com.tinuproject.tinu.domain.member.exception.NotExistCustomFilter
import com.tinuproject.tinu.domain.member.exception.NotExistMemberException
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.infra.swagger.example.SelectCustomFilterExam
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/custom-filter")
@Tag(name="커스텀 필터 API", description = "커스텀 필터와 관련한 API입니다.")
class CustomFilterController(
    private val customFilterService: CustomFilterService
) : CustomFilterSwaggerDocs {
    val log : Logger = LoggerFactory.getLogger(this::class.java)


    @GetMapping("")
    override fun requestCustomFilter(@AuthenticationPrincipal userId : UUID) : ResponseEntity<ResponseDTO<List<SelectCustomFilter>?>> {
        return ResponseEntityGenerator.onSuccess(customFilterService.getCustomFilter(userId))
    }

    @PostMapping("")
    override fun createCustomFilter(@AuthenticationPrincipal userId : UUID, @RequestBody createCustomFilter: CreateCustomFilter) : ResponseEntity<ResponseDTO<NullResponse?>>{
        customFilterService.createCustomFilter(userId = userId, createCustomFilter= createCustomFilter)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{filterId}")
    override fun updateCustomFilter(@AuthenticationPrincipal userId : UUID, @PathVariable(name = "filterId") filterId : Long, @RequestBody updateCustomFilter: UpdateCustomFilter) : ResponseEntity<ResponseDTO<NullResponse?>>{
        updateCustomFilter.filterId = filterId

        customFilterService.updateCustomFilter(userId, updateCustomFilter)

        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{filterId}")
    override fun deleteCustomFilter(@AuthenticationPrincipal userId : UUID, @PathVariable(name = "filterId") filterId: Long) : ResponseEntity<ResponseDTO<NullResponse?>>{
        customFilterService.deleteCustomFilter(userId, DeleteCustomFilter(filterId=filterId))

        return ResponseEntityGenerator.onSuccess()
    }
}