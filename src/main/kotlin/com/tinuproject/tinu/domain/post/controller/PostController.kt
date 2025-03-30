package com.tinuproject.tinu.domain.post.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.post.dto.request.PostCreateRequest
import com.tinuproject.tinu.domain.post.dto.request.PostDeleteRequest
import com.tinuproject.tinu.domain.post.dto.request.PostUpdateRequest
import com.tinuproject.tinu.domain.post.dto.response.PostCreateResponse
import com.tinuproject.tinu.domain.post.dto.response.PostDetailResponse
import com.tinuproject.tinu.domain.post.dto.response.PostsListResponse
import com.tinuproject.tinu.domain.post.service.PostService
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/post")
class PostController(
        private val postService: PostService
) {
    @GetMapping
    fun getPosts(
            @AuthenticationPrincipal userId: UUID,
            @RequestParam(required = false) cursorId: String?,
            @RequestParam(required = false) keyword: String?,
            @RequestParam(required = false) category: List<Long>?,
            @RequestParam(required = false) minPrice: Int?,
            @RequestParam(required = false) maxPrice: Int?,
            @RequestParam onlySell: Boolean
    ): ResponseEntity<ResponseDTO<PostsListResponse?>> {
        return ResponseEntityGenerator.onSuccess(postService.getPostList(
                userId,
                cursorId,
                keyword,
                category,
                minPrice,
                maxPrice,
                onlySell
        ))
    }

    @GetMapping("/{postId}")
    fun getPostDetail(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<PostDetailResponse?>> {
        return ResponseEntityGenerator.onSuccess(postService.getPostDetail(userId, postId))
    }

    @PostMapping
    fun createPost(
            @AuthenticationPrincipal userId: UUID,
            @RequestBody postCreateRequest: PostCreateRequest
    ): ResponseEntity<ResponseDTO<PostCreateResponse?>> {
        return ResponseEntityGenerator.onSuccess(postService.createPost(userId, postCreateRequest))
    }

    @PutMapping("/{postId}")
    fun updatePost(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long,
            @RequestBody postUpdateRequest: PostUpdateRequest
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.updatePost(userId, postId, postUpdateRequest)
        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.deletePost(userId, PostDeleteRequest(postId = postId))
        return ResponseEntityGenerator.onSuccess()
    }

    @PostMapping("/{postId}/scrap")
    fun createPostScrap(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.createPostScrap(userId, postId)
        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{postId}/scrap")
    fun deletePostScrap(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.deletePostScrap(userId, postId)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{postId}/status")
    fun updatePostStatus(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long,
            @RequestParam(required = true) isSell: Boolean
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.updatePostStatus(userId, postId, isSell)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{postId}/hide")
    fun updatePostHide(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long,
            @RequestParam(required = true) isHide: Boolean
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.updatePostHide(userId, postId, isHide)
        return ResponseEntityGenerator.onSuccess()
    }

}