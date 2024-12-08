package com.tinuproject.tinu.s3.dto.request

data class S3UploadAbortRequest(
        val key: String,
        val uploadId: String
)