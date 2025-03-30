package com.tinuproject.tinu.s3.dto.request

data class S3PresignedUrlRequest(
        val contents: MutableList<Object>
) {
    data class Object(
            val contentType: String,
            val contentLength: Long
    )
}