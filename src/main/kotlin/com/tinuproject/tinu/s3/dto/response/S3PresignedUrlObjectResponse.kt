package com.tinuproject.tinu.s3.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "S3 Presigned URL 객체 응답")
data class S3PresignedUrlObjectResponse(
        @Schema(description = "Presigned URL", defaultValue = "https://tinubucket.s3.ap-northeast-2.amazonaws.com/original/1741624074269_77db2d4a-44fe-4cb1-a638-d925c3fb59b8_8.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250310T162754Z&X-Amz-SignedHeaders=content-length%3Bcontent-type%3Bhost&X-Amz-Credential=AKIASVLKCLKYXWX6Z2VX%2F20250310%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=600&X-Amz-Signature=1f777c659a938cb29c2f4661289af7bab6386b1c0da428d772ea3cd619ca03dd")
        val presignedUrl: String,
        @Schema(description = "S3 Object Key", defaultValue = "original/1741624074269_77db2d4a-44fe-4cb1-a638-d925c3fb59b8_8.png")
        val key: String
)