package com.tinuproject.tinu.domain.post.dto.request

import com.tinuproject.tinu.domain.enums.PaymentMethod
import com.tinuproject.tinu.domain.enums.SellMethod
import com.tinuproject.tinu.s3.dto.request.S3VerifiableRequest
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 수정 요청")
data class PostUpdateRequest(
        @Schema(description = "게시글 제목", defaultValue = "아이폰 13 미니 판매합니다")
        val title: String,
        @Schema(description = "게시글 내용", defaultValue = "아이폰 13 미니 128GB 블랙 판매합니다.\\n제품상태 잔기스 한 군데 존재합니다.\\n거슬리지 않으신 분들만 연락주세요!!!")
        val body: String,
        @Schema(description = "카테고리 ID", defaultValue = "10")
        val categoryId: Long,
        @Schema(description = "가격", defaultValue = "600000")
        val price: Int,
        @Schema(description = "거래 방법", defaultValue = "[0]")
        val sellMethod: Set<SellMethod> = setOf(),
        @Schema(description = "결제 방법", defaultValue = "[0, 1]")
        val paymentMethod: Set<PaymentMethod> = setOf(),
        val images: List<S3VerifiableRequest>,
        @Schema(description = "해시태그", defaultValue = "[\"아이폰\", \"미니\"]")
        val hashTags: List<String>
)