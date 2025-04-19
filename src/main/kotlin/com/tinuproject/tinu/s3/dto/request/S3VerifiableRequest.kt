package com.tinuproject.tinu.s3.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "S3 검증 요청")
data class S3VerifiableRequest (
    @Schema(description = "S3 Object Key", defaultValue = "original/1741624074269_77db2d4a-44fe-4cb1-a638-d925c3fb59b8_8.png")
    val key: String,

    @get:JsonProperty("ETag")
    @Schema(description = "S3 Object Version ID", defaultValue = "9580dee05fa2a7d1b6d70192093c09b3")
    val ETag: String
)