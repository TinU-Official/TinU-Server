package com.tinuproject.tinu.s3.dto.response

import java.time.LocalDateTime

class S3PresignedUrlResponse(
        val objects: MutableList<Object>,
        val expiration: LocalDateTime
) {
    data class Object(
            val presignedUrl: String,
            val key: String
    )
}