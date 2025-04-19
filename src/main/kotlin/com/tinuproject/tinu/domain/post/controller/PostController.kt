package com.tinuproject.tinu.domain.post.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.mail.NotExistMemberException
import com.tinuproject.tinu.domain.exception.post.*
import com.tinuproject.tinu.domain.exception.s3.*
import com.tinuproject.tinu.domain.exception.scrap.ScrapAlreadyExistException
import com.tinuproject.tinu.domain.exception.scrap.ScrapNotFoundException
import com.tinuproject.tinu.domain.post.dto.request.PostCreateRequest
import com.tinuproject.tinu.domain.post.dto.request.PostDeleteRequest
import com.tinuproject.tinu.domain.post.dto.request.PostUpdateRequest
import com.tinuproject.tinu.domain.post.dto.response.PostCreateResponse
import com.tinuproject.tinu.domain.post.dto.response.PostDetailResponse
import com.tinuproject.tinu.domain.post.dto.response.PostsListResponse
import com.tinuproject.tinu.domain.post.service.PostService
import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/post")
@Tag(name = "Post API", description = "Post API")
class PostController(
        private val postService: PostService
) {
    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                NotExistMemberException::class
            ]
    )
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
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                NotExistMemberException::class,
                PostNotFoundException::class,
                UniversityNotMatchException::class,
                PostHiddenException::class
            ]
    )
    fun getPostDetail(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<PostDetailResponse?>> {
        return ResponseEntityGenerator.onSuccess(postService.getPostDetail(userId, postId))
    }

    @PostMapping
    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                NotExistMemberException::class,
                CategoryNotFoundException::class,
                NoSuchKeyException::class,
                InvalidETagException::class
            ]
    )
    fun createPost(
            @AuthenticationPrincipal userId: UUID,
            @RequestBody postCreateRequest: PostCreateRequest
    ): ResponseEntity<ResponseDTO<PostCreateResponse?>> {
        return ResponseEntityGenerator.onSuccess(postService.createPost(userId, postCreateRequest))
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                PostNotFoundException::class,
                AuthorNotMatchException::class,
                CategoryNotFoundException::class,
                UploadSizeOutOfRangeException::class,
                NoSuchKeyException::class,
                InvalidETagException::class,
            ]
    )
    fun updatePost(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long,
            @RequestBody postUpdateRequest: PostUpdateRequest
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.updatePost(userId, postId, postUpdateRequest)
        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                PostNotFoundException::class,
                AuthorNotMatchException::class
            ]
    )
    fun deletePost(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.deletePost(userId, PostDeleteRequest(postId = postId))
        return ResponseEntityGenerator.onSuccess()
    }

    @PostMapping("/{postId}/scrap")
    @Operation(summary = "게시글 스크랩", description = "게시글을 스크랩합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                NotExistMemberException::class,
                PostNotFoundException::class,
                UniversityNotMatchException::class,
                ScrapAlreadyExistException::class
            ]
    )
    fun createPostScrap(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.createPostScrap(userId, postId)
        return ResponseEntityGenerator.onSuccess()
    }

    @DeleteMapping("/{postId}/scrap")
    @Operation(summary = "게시글 스크랩 해제", description = "게시글 스크랩을 해제합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                NotExistMemberException::class,
                ScrapNotFoundException::class
            ]
    )
    fun deletePostScrap(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.deletePostScrap(userId, postId)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{postId}/status")
    @Operation(summary = "게시글 상태 변경", description = "게시글의 상태를 변경합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                PostNotFoundException::class,
                AuthorNotMatchException::class
            ]
    )
    fun updatePostStatus(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long,
            @RequestParam(required = true) isSell: Boolean
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.updatePostStatus(userId, postId, isSell)
        return ResponseEntityGenerator.onSuccess()
    }

    @PutMapping("/{postId}/hide")
    @Operation(summary = "게시글 숨김 처리", description = "게시글을 숨김 처리합니다.")
    @SwaggerExceptionResponses(
            exceptions = [
                PostNotFoundException::class,
                AuthorNotMatchException::class
            ]
    )
    fun updatePostHide(
            @AuthenticationPrincipal userId: UUID,
            @PathVariable postId: Long,
            @RequestParam(required = true) isHide: Boolean
    ): ResponseEntity<ResponseDTO<NullResponse?>> {
        postService.updatePostHide(userId, postId, isHide)
        return ResponseEntityGenerator.onSuccess()
    }

}