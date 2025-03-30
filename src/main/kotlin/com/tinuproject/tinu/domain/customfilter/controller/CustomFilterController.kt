package com.tinuproject.tinu.domain.customfilter.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.CreateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.DeleteCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.response.SelectCustomFilter
import com.tinuproject.tinu.domain.customfilter.service.CustomFilterService
import com.tinuproject.tinu.domain.member.service.MemberService
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/custom-filter")
class CustomFilterController(
    private val customFilterService: CustomFilterService
) {
    val log : Logger = LoggerFactory.getLogger(this::class.java)


    @GetMapping("")
    fun requestCustomFilter(@AuthenticationPrincipal userId : UUID) : ResponseEntity<ResponseDTO<List<SelectCustomFilter>?>> {
        return ResponseEntityGenerator.onSuccess(customFilterService.getCustomFilter(userId))
    }

    @PostMapping("")
    fun createCustomFilter(@AuthenticationPrincipal userId : UUID, @RequestBody createCustomFilter: CreateCustomFilter) : ResponseEntity<ResponseDTO<NullResponse?>>{
        customFilterService.createCustomFilter(userId = userId, createCustomFilter= createCustomFilter)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{filterId}")
    fun updateCustomFilter(@AuthenticationPrincipal userId : UUID, @PathVariable(name = "filterId") filterId : Long,@RequestBody updateCustomFilter: UpdateCustomFilter) : ResponseEntity<ResponseDTO<NullResponse?>>{
        updateCustomFilter.filterId = filterId

        customFilterService.updateCustomFilter(userId, updateCustomFilter)

        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{filterId}")
    fun deleteCustomFilter(@AuthenticationPrincipal userId : UUID, @PathVariable(name = "filterId") filterId: Long) : ResponseEntity<ResponseDTO<NullResponse?>>{
        customFilterService.deleteCustomFilter(userId, DeleteCustomFilter(filterId=filterId))

        return ResponseEntityGenerator.onSuccess()
    }
}