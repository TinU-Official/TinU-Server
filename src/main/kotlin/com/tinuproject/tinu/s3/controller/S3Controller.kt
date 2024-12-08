package com.tinuproject.tinu.s3.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.s3.dto.request.*
import com.tinuproject.tinu.s3.service.S3ServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/multimedia")
class S3Controller(
        private val s3ServiceImpl: S3ServiceImpl,
) {

    @PostMapping("/initiateUpload")
    fun initiateUpload(@RequestBody s3UploadInitiateRequest: S3UploadInitiateRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3ServiceImpl.initiateUpload(s3UploadInitiateRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/presigned-url")
    fun getUploadPresignedUrl(@RequestBody s3UploadPresignedUrlRequest: S3UploadPresignedUrlRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3ServiceImpl.getUploadPreSignedUrl(s3UploadPresignedUrlRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/complete-upload")
    fun completeUpload(@RequestBody s3UploadCompleteRequest: S3UploadCompleteRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3ServiceImpl.completeUpload(s3UploadCompleteRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/abort-upload")
    fun abortUpload(@RequestBody s3UploadAbortRequest: S3UploadAbortRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3ServiceImpl.abortUpload(s3UploadAbortRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody s3DeleteRequest: S3DeleteRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3ServiceImpl.deleteObject(s3DeleteRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }
}