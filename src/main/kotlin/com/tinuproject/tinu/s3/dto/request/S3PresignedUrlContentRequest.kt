package com.tinuproject.tinu.s3.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "S3 Presigned URL Content 요청")
data class S3PresignedUrlContentRequest(
        @Schema(description = "Content Type", defaultValue = "image/png")
        val contentType: String,
        @Schema(description = "Content Length", defaultValue = "4225", type = "long")
        val contentLength: Long
)
