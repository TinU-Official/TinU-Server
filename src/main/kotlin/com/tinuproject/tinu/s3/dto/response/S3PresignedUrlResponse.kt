package com.tinuproject.tinu.s3.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "S3 Presigned URL 응답")
class S3PresignedUrlResponse(
        @Schema(description = "Presigned URL List")
        val objects: MutableList<S3PresignedUrlObjectResponse>,
        @Schema(description = "Expiration date", defaultValue = "2025-03-11T01:37:54.2730015", type = "string")
        val expiration: LocalDateTime
)