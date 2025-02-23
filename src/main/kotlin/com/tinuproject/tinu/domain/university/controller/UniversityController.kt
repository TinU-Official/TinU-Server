package com.tinuproject.tinu.domain.university.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.university.service.UniversityService
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api/university")
class UniversityController(
    val universityService: UniversityService
) {

    @GetMapping("/test")
    fun kyonggiUniverSityAdd() : ResponseEntity<ResponseDTO>{
        universityService.testUniversityAdd()

        return ResponseEntityGenerator.onSuccess(null)
    }


}