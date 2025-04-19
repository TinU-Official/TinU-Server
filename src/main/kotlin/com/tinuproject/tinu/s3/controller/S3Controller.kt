package com.tinuproject.tinu.s3.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.s3.FileLengthOutOfRangeException
import com.tinuproject.tinu.domain.exception.s3.NotAllowedExtensionException
import com.tinuproject.tinu.domain.exception.s3.UploadSizeOutOfRangeException
import com.tinuproject.tinu.s3.dto.request.*
import com.tinuproject.tinu.s3.dto.response.S3PresignedUrlResponse
import com.tinuproject.tinu.s3.service.S3Service
import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.web.ResponseEntityGenerator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("api/image")
@Tag(name = "S3 API", description = "S3 API")
class S3Controller(
        private val s3Service: S3Service,
) {
    @PostMapping("/presigned-url")
    @Operation(summary = "Presigned URL 생성", description = "Presigned URL을 생성합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                FileLengthOutOfRangeException::class,
                NotAllowedExtensionException::class,
                UploadSizeOutOfRangeException::class
            ]
    )
    suspend fun getUploadPresignedUrl(@AuthenticationPrincipal userId: UUID, @RequestBody s3PresignedUrlRequest: S3PresignedUrlRequest): ResponseEntity<ResponseDTO<S3PresignedUrlResponse?>> {

        return ResponseEntityGenerator.onSuccess(s3Service.getPreSignedUrl(s3PresignedUrlRequest))
    }
}