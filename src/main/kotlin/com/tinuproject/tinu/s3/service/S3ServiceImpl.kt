package com.tinuproject.tinu.s3.service

import com.tinuproject.tinu.domain.exception.s3.*
import com.tinuproject.tinu.domain.exception.s3.NoSuchKeyException
import com.tinuproject.tinu.s3.dto.request.S3VerifiableRequest
import com.tinuproject.tinu.s3.dto.request.*
import com.tinuproject.tinu.s3.dto.response.S3PresignedUrlObjectResponse
import com.tinuproject.tinu.s3.dto.response.S3PresignedUrlResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class S3ServiceImpl(
        @Value("\${aws.s3.bucket}")
        private val bucketName: String,
        private val s3Client: S3Client,
        private val s3Presigner: S3Presigner,
        @Value("\${cloudfront.domain}")
        private var cloudFrontDomain: String
) : S3Service {

    val log : Logger = LoggerFactory.getLogger(this::class.java)
    override suspend fun getPreSignedUrl(s3PresignedUrlRequest: S3PresignedUrlRequest): S3PresignedUrlResponse = withContext(Dispatchers.IO) {

        val contents = s3PresignedUrlRequest.contents
        if (contents.size !in 1..10)
            throw UploadSizeOutOfRangeException()

        contents.forEach { content ->
            if (content.contentType !in ALLOWED_EXTENSIONS)
                throw NotAllowedExtensionException()
            if (content.contentLength !in 1..(1024 * 1024 * 10))
                throw FileLengthOutOfRangeException()
        }

        val currentTimeMillis = System.currentTimeMillis()
        val uuid = UUID.randomUUID()
        val expiration = Duration.ofMinutes(10)

        val objects = contents.mapIndexed() { index, content ->
            async {
                val extension = content.contentType.split("/")[1]
                val key = "original/${currentTimeMillis}_${uuid}_${index}.${extension}"

                val presignedPutObjectRequest: PresignedPutObjectRequest = s3Presigner.presignPutObject(PutObjectPresignRequest.builder()
                        .signatureDuration(expiration)
                        .putObjectRequest {
                            it.bucket(bucketName)
                                    .contentType(content.contentType)
                                    .contentLength(content.contentLength)
                                    .key(key)
                        }
                        .build()
                )

                S3PresignedUrlObjectResponse(
                        presignedUrl = presignedPutObjectRequest.url().toString(),
                        key = key
                )
            }
        }.awaitAll().toMutableList()

        //응답 DTO 생성 및 반환
        S3PresignedUrlResponse(
                objects = objects,
                expiration = LocalDateTime.now().plus(expiration)
        )
    }

    companion object {
        private val ALLOWED_EXTENSIONS = listOf("image/jpg", "image/jpeg", "image/png", "image/webp")
    }

    override suspend fun verifyImages(objects: List<S3VerifiableRequest>): List<String> = withContext(Dispatchers.IO) {
        val urls = objects.map { obj ->
            async {
                val response: HeadObjectResponse
                try {
                    response = s3Client.headObject(HeadObjectRequest.builder()
                            .bucket(bucketName)
                            .key(obj.key)
                            .build())
                } catch (e: software.amazon.awssdk.services.s3.model.NoSuchKeyException) {
                    throw NoSuchKeyException()
                }
                log.info(response.eTag())
                log.info(obj.ETag)
                if (response.eTag().trim('"') != obj.ETag) {
                    throw InvalidETagException()
                }

                "${cloudFrontDomain}/${obj.key}"
            }
        }.awaitAll().toMutableList()

        urls
    }

    override fun verifyImage(obj: S3VerifiableRequest): String {
        val response: HeadObjectResponse
        try {
            response = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(obj.key)
                    .build())
        } catch (e: software.amazon.awssdk.services.s3.model.NoSuchKeyException) {
            throw NoSuchKeyException()
        }
        log.info(response.eTag())
        log.info(obj.ETag)
        if (response.eTag().trim('"') != obj.ETag) {
            throw InvalidETagException()
        }

        return "${cloudFrontDomain}/${obj.key}"
    }

    override fun removeImages(objects: List<String>) {
        val keys = objects.map { url ->
            url.removePrefix("${cloudFrontDomain}/")
        }
        s3Client.deleteObjects(DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder()
                        .objects(keys.map { ObjectIdentifier.builder().key(it).build() })
                        .build())
                .build()
        )
    }
}