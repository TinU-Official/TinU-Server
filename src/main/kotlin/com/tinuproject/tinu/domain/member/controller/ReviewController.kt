package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.domain.member.controller.docs.ReviewSwaggerDocs
import com.tinuproject.tinu.domain.member.controller.dto.request.CreateReviewRequest
import com.tinuproject.tinu.domain.member.controller.dto.request.SearchWriteReviewRequest
import com.tinuproject.tinu.domain.member.exception.ExistReviewException
import com.tinuproject.tinu.domain.member.service.ReviewService
import com.tinuproject.tinu.domain.member.service.dto.input.SearchWriteReviewInput
import com.tinuproject.tinu.domain.post.exception.PostNotFoundException
import com.tinuproject.tinu.global.exception.ForbiddenException
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("api/reviews")
class ReviewController(
    private val reviewService: ReviewService
) : ReviewSwaggerDocs {

    @PostMapping("")
    override fun createReview(@AuthenticationPrincipal userId : UUID, @RequestBody request: CreateReviewRequest) : ResponseEntity<ResponseDTO<Boolean?>> {
        return ResponseEntityGenerator.onSuccess(reviewService.createReview(request.of(userId)))
    }

    @GetMapping("")
    override fun checkAlreadyWriteReview(@AuthenticationPrincipal userId : UUID, @RequestParam postId: Long):ResponseEntity<ResponseDTO<Boolean?>>{
        return ResponseEntityGenerator.onSuccess(reviewService.needWrittenReview(SearchWriteReviewInput(
            userId = userId,
            postId = postId
        )))
    }
}