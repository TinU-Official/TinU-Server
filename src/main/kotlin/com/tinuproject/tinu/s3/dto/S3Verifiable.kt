package com.tinuproject.tinu.s3.dto

interface S3Verifiable {
    val key: String
    val ETag: String
}