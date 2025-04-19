package com.tinuproject.tinu.domain.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "해시태그 응답")
data class PostHashTagResponse(
        @Schema(description = "해시태그 ID", defaultValue = "1")
        val id: Long,
        @Schema(description = "해시태그 이름", defaultValue = "아이폰")
        val name: String
)
