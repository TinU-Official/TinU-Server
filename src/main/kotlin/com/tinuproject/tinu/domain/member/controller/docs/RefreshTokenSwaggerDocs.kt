package com.tinuproject.tinu.domain.member.controller.docs

import com.tinuproject.tinu.domain.member.exception.ExpiredTokenException
import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import jakarta.servlet.http.HttpServletResponse

interface RefreshTokenSwaggerDocs {

    @Operation(
        summary = "AccessToken 재발급 API",
        description = "RefreshToken을 통해 AccessToken을 재발급 받는 로직입니다."
    )
    @SwaggerExceptionResponses(
        exceptions = [
            NotFoundTokenException::class,
            InvalidedTokenException::class,
            ExpiredTokenException::class
        ]
    )
    fun refreshAccessToken(
        httpServletResponse: HttpServletResponse,
        @CookieValue(name = "RefreshToken") refreshToken: String?
    ): ResponseEntity<ResponseDTO<NullResponse?>>
}
