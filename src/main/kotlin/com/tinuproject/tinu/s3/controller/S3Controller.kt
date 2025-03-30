package com.tinuproject.tinu.s3.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.s3.dto.request.*
import com.tinuproject.tinu.s3.dto.response.S3PresignedUrlResponse
import com.tinuproject.tinu.s3.service.S3Service
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("api/image")
class S3Controller(
        private val s3Service: S3Service,
) {
    @PostMapping("/presigned-url")
    suspend fun getUploadPresignedUrl(@AuthenticationPrincipal userId: UUID, @RequestBody s3PresignedUrlRequest: S3PresignedUrlRequest): ResponseEntity<ResponseDTO<S3PresignedUrlResponse?>> {

        return ResponseEntityGenerator.onSuccess(s3Service.getPreSignedUrl(s3PresignedUrlRequest))
    }
}