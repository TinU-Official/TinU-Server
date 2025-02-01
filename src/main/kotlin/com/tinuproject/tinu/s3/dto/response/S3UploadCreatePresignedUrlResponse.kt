package com.tinuproject.tinu.s3.dto.response

import java.time.LocalDateTime

data class S3UploadCreatePresignedUrlResponse(
        val presignedUrl: String,       // 생성된 presigned URL
        val uploadId: String,           // 멀티파트 업로드 ID
        val partNumber: Int,            // 현재 요청된 파트 번호
        val expiration: LocalDateTime   // Presigned URL 만료 시간
)