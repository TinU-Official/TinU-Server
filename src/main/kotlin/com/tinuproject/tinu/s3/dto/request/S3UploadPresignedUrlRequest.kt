package com.tinuproject.tinu.s3.dto.request

data class S3UploadPresignedUrlRequest(
        val key: String,
        val uploadId: String,
        val partNumber: Int
)