package com.tinuproject.tinu.domain.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "게시글 리스트 응답")
data class PostsListResponse (
        val posts: List<PostListBodyResponse>,
        @Schema(description = "게시글 수", defaultValue = "10")
        val size: Int,
        @Schema(description = "다음 커서 ID", defaultValue = "17")
        val nextCursorId: String
)