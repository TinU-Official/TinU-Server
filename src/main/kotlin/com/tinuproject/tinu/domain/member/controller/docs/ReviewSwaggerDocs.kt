package com.tinuproject.tinu.domain.member.controller.docs

import com.tinuproject.tinu.domain.member.controller.dto.request.CreateReviewRequest
import com.tinuproject.tinu.domain.member.exception.ExistReviewException
import com.tinuproject.tinu.domain.post.exception.PostNotFoundException
import com.tinuproject.tinu.global.exception.ForbiddenException
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Tag(name = "리뷰(거래 이후 평가) API", description = "리뷰(거래 이후 평가) 관련 API입니다.")
interface ReviewSwaggerDocs {

    @Operation(
        summary = "거래 리뷰 작성 API",
        description = "거래 리뷰 작성 API입니다." +
                "<br>필수 파라미터 : postId, mainEvaluation, subEvaluation"
    )
    @SwaggerExceptionResponses(
        exceptions = [
            ForbiddenException::class,
            PostNotFoundException::class,
            ExistReviewException::class
        ]
    )
    fun createReview(
        @AuthenticationPrincipal userId: UUID,
        @RequestBody request: CreateReviewRequest
    ): ResponseEntity<ResponseDTO<Boolean?>>

    @Operation(
        summary = "거래 리뷰 필요 여부 확인 API",
        description = "리뷰 작성 필요 여부를 확인하는 API입니다." +
                "<br>true : 리뷰 작성 필요 / false : 리뷰 작성 불필요"
    )
    @SwaggerExceptionResponses(
        exceptions = [PostNotFoundException::class]
    )
    fun checkAlreadyWriteReview(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam postId: Long
    ): ResponseEntity<ResponseDTO<Boolean?>>
}
