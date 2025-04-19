package com.tinuproject.tinu.s3.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "S3 Presigned URL 요청")
data class S3PresignedUrlRequest(
        @Schema(description = "Content List")
        val contents: MutableList<S3PresignedUrlContentRequest>
)