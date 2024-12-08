package com.tinuproject.tinu.s3.dto.request

data class S3UploadInitiateRequest(
        val originalFileName: String,
        val fileType: String,
        val fileSize: Long,
)