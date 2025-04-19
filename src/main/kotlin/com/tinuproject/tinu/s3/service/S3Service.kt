package com.tinuproject.tinu.s3.service

import com.tinuproject.tinu.s3.dto.request.S3VerifiableRequest
import com.tinuproject.tinu.s3.dto.request.*
import com.tinuproject.tinu.s3.dto.response.S3PresignedUrlResponse

interface S3Service {
    suspend fun getPreSignedUrl(s3PresignedUrlRequest: S3PresignedUrlRequest): S3PresignedUrlResponse

    suspend fun verifyImages(objects: List<S3VerifiableRequest>): List<String>

    fun verifyImage(obj: S3VerifiableRequest): String

    fun removeImages(objects: List<String>)

}