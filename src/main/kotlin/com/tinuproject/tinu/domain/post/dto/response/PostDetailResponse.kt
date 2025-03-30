package com.tinuproject.tinu.domain.post.dto.response

import com.tinuproject.tinu.domain.enums.PaymentMethod
import com.tinuproject.tinu.domain.enums.SellMethod
import java.time.LocalDateTime

data class PostDetailResponse(
        val postId: Long,
        val date: LocalDateTime,
        val title: String,
        val body: String,
        val memberId: Long,
        val nickname: String,
        val profile: String?,
        val categoryId: Long,
        val price: Int,
        val sellMethod: Set<SellMethod>,
        val paymentMethod: Set<PaymentMethod>,
        val isSell: Boolean,
        val isLike: Boolean,
        val likeCount: Long,
        val isWriter: Boolean,
        val images: List<String>,
        val postHashTagMap: List<HashTag>
) {
    data class HashTag(
            val hashTagId: Long,
            val hashTagName: String
    )
}