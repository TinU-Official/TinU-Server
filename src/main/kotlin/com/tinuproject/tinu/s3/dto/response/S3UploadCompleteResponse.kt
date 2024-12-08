package com.tinuproject.tinu.s3.dto.response

data class S3UploadCompleteResponse(
        val fileName: String,   //S3 오브젝트의 이름
        val url: String,        //S3 오브젝트의 CDN 주소
        val fileSize: Long      //S3 오브젝트의 크기
)