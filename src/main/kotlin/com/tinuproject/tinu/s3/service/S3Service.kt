package com.tinuproject.tinu.s3.service

import com.tinuproject.tinu.s3.dto.request.*
import com.tinuproject.tinu.s3.dto.response.S3UploadCompleteResponse
import com.tinuproject.tinu.s3.dto.response.S3UploadCreatePresignedUrlResponse
import com.tinuproject.tinu.s3.dto.response.S3UploadInitiateResponse

interface S3Service {
    fun initiateUpload(s3UploadInitiateRequest: S3UploadInitiateRequest): S3UploadInitiateResponse

    fun getUploadPreSignedUrl(s3UploadPresignedUrlRequest: S3UploadPresignedUrlRequest): S3UploadCreatePresignedUrlResponse

    fun completeUpload(s3UploadCompleteRequest: S3UploadCompleteRequest): S3UploadCompleteResponse

    fun abortUpload(s3UploadAbortRequest: S3UploadAbortRequest)

    fun deleteObject(s3DeleteRequest: S3DeleteRequest)

    fun getFileSizeFromS3Url(bucketName: String, fileName: String): Long
}