package com.tinuproject.tinu.s3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config(
        @Value("\${spring.cloud.aws.credentials.access-key}")
        private val accessKey: String,
        @Value("\${spring.cloud.aws.credentials.secret-key}")
        private val secretKey: String,
        @Value("\${spring.cloud.aws.region.static}")
        private val region: String
) {

    @Bean
    fun basicAWSCredentials(): AwsCredentials {
        return AwsBasicCredentials.create(accessKey, secretKey)
    }

    @Bean
    fun s3Client(
            awsCredentials: AwsCredentials
    ): S3Client {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build()
    }

    @Bean
    fun s3Presigner(
            awsCredentials: AwsCredentials
    ): S3Presigner {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build()
    }
}