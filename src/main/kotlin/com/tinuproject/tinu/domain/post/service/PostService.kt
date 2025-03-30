package com.tinuproject.tinu.domain.post.service

import com.tinuproject.tinu.domain.post.dto.request.PostCreateRequest
import com.tinuproject.tinu.domain.post.dto.request.PostDeleteRequest
import com.tinuproject.tinu.domain.post.dto.request.PostUpdateRequest
import com.tinuproject.tinu.domain.post.dto.response.PostCreateResponse
import com.tinuproject.tinu.domain.post.dto.response.PostDetailResponse
import com.tinuproject.tinu.domain.post.dto.response.PostsListResponse
import java.util.UUID

interface PostService {
    fun getPostList(
            userId: UUID,
            cursorId: String?,
            keyword: String?,
            category: List<Long>?,
            minPrice: Int?,
            maxPrice: Int?,
            onlySell: Boolean
    ): PostsListResponse

    fun getPostDetail(
            userId: UUID,
            postId: Long
    ): PostDetailResponse

    fun createPost(
            userId: UUID,
            postCreateRequest: PostCreateRequest
    ): PostCreateResponse

    fun updatePost(
            userId: UUID,
            postId: Long,
            postUpdateRequest: PostUpdateRequest
    )

    fun deletePost(
            userId: UUID,
            postDeleteRequest: PostDeleteRequest
    )

    fun createPostScrap(
            userId: UUID,
            postId: Long
    )

    fun deletePostScrap(
            userId: UUID,
            postId: Long
    )

    fun updatePostStatus(
            userId: UUID,
            postId: Long,
            isSell: Boolean
    )

    fun updatePostHide(
            userId: UUID,
            postId: Long,
            isHide: Boolean
    )
}