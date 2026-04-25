package com.tinuproject.tinu.domain.post.controller.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "게시글 응답")
data class PostListBodyResponse(
        @Schema(description = "게시글 ID", defaultValue = "1")
        val id: Long?,
        @Schema(description = "작성시간", defaultValue = "2025-04-01T21:41:15")
        val createdAt: LocalDateTime?,
        @Schema(description = "판매글 제목", defaultValue = "전자레인지 판매합니다")
        val title: String,
        @Schema(description = "가격", defaultValue = "130000")
        val price: Int,
        @Schema(description = "썸네일 URL", defaultValue = "https://d3sbb3b89xu3eb.cloudfront.net/original/1740896957331_c30cc9a7-4715-44fc-9049-62ad118943e7_0.png")
        val thumbnail: String?,
        @Schema(description = "판매글 좋아요 여부", defaultValue = "false")
        val isLike: Boolean,
        @Schema(description = "판매글 좋아요 수", defaultValue = "10")
        val likeCount: Long,
        @Schema(description = "판매글 판매 완료 여부 (true=판매완료)", defaultValue = "false")
        val isSoldOut: Boolean
)
