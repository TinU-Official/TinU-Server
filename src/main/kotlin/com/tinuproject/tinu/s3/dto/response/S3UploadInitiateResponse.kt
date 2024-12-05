package com.tinuproject.tinu.s3.dto.response

data class S3UploadInitiateResponse(
        val uploadId: String,   //멀티파트 업로드 ID
        val key: String         //S3 오브젝트의 경로
)