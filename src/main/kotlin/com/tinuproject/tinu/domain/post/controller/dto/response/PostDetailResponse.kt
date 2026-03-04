package com.tinuproject.tinu.domain.post.controller.dto.response

import com.tinuproject.tinu.domain.post.enums.PaymentMethod
import com.tinuproject.tinu.domain.post.enums.SellMethod
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "판매글 상세 조회 응답")
data class PostDetailResponse(
    @Schema(description = "판매글 ID", defaultValue = "21")
        val postId: Long,
    @Schema(description = "판매글 작성일", defaultValue = "2025-04-02T18:27:17.329378")
        val date: LocalDateTime,
    @Schema(description = "판매글 제목", defaultValue = "아이폰 13 미니 판매합니다")
        val title: String,
    @Schema(description = "판매글 내용", defaultValue = "아이폰 13 미니 128GB 블랙 판매합니다.\\n제품상태 잔기스 한 군데 존재합니다.\\n거슬리지 않으신 분들만 연락주세요!!!")
        val body: String,
    @Schema(description = "판매글 작성자 ID", defaultValue = "2")
        val memberId: Long,
    @Schema(description = "판매글 작성자 이름", defaultValue = "홍길동")
        val nickname: String,
    @Schema(description = "판매글 작성자 프로필 이미지 URL", defaultValue = "https://d3sbb3b89xu3eb.cloudfront.net/original/1743585952663_c83680bc-1d99-4cf0-b0c1-b18acd6d1922_0.png")
        val profile: String?,
    @Schema(description = "카테고리 ID", defaultValue = "10")
        val categoryId: Long,
    @Schema(description = "가격", defaultValue = "600000")
        val price: Int,
    @Schema(description = "거래 방법", defaultValue = "[0]")
        val sellMethod: Set<SellMethod>,
    @Schema(description = "결제 방법", defaultValue = "[0, 1]")
        val paymentMethod: Set<PaymentMethod>,
    @Schema(description = "판매글 판매 완료 상태(false = 판매 중, true = 판매 완료)", defaultValue = "false")
        val isSoldOut: Boolean,
    @Schema(description = "판매글 좋아요 여부(true = 좋아요 상태)", defaultValue = "true")
        val isLike: Boolean,
    @Schema(description = "판매글 좋아요 수", defaultValue = "10")
        val likeCount: Long,
    @Schema(description = "작성자 여부", defaultValue = "true")
        val isWriter: Boolean,
    @Schema(description = "판매글 이미지 URL", defaultValue = "[\"https://d3sbb3b89xu3eb.cloudfront.net/original/1740896957331_c30cc9a7-4715-44fc-9049-62ad118943e7_0.png\"]")
        val images: List<String>,
    @Schema(description = "판매글 해시태그", defaultValue = "[{\"id\": 1, \"name\": \"아이폰\"}, {\"id\": 2, \"name\": \"미니\"}]")
        val hashTags: List<PostHashTagResponse>
)