package com.tinuproject.tinu.domain.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 삭제 요청")
data class PostDeleteRequest(
        @Schema(description = "게시글 ID", defaultValue = "1")
        val postId: Long
)