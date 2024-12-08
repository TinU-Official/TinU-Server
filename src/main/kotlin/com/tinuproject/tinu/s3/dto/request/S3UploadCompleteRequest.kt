package com.tinuproject.tinu.s3.dto.request

data class S3UploadCompleteRequest(
        val key: String,
        val uploadId: String,
        val parts: List<Part>,
) {
    data class Part(
            val partNumber: Int,
            val eTag: String,
    )
}