package com.tinuproject.tinu.domain.token.refreshtoken.controller

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.token.NotFoundTokenException
import com.tinuproject.tinu.domain.token.Tokens
import com.tinuproject.tinu.domain.token.refreshtoken.service.RefreshTokenService
import com.tinuproject.tinu.web.CookieGenerator
import com.tinuproject.tinu.web.NullResponse
import com.tinuproject.tinu.web.ResponseEntityGenerator
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/token")
class RefreshTokenController(
    private val refreshTokenService : RefreshTokenService,

    @Value("\${cookie.token.refresh-token}")
    private val refreshTokenkey : String,
) {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    @GetMapping("/refresh")
    fun refreshAccessToken(httpServletResponse: HttpServletResponse, @CookieValue(name = "RefreshToken") refreshToken : String?): ResponseEntity<ResponseDTO<NullResponse?>> {
        log.info("AccessToken 갱신 시도")

        refreshToken?:throw NotFoundTokenException()

        val tokens : Tokens = refreshTokenService.reissueAccessTokenByRefreshToken(refreshToken)

        httpServletResponse.addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ tokens.accessToken)
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE,CookieGenerator.createCookies(refreshTokenkey, tokens.refreshToken))

        return ResponseEntityGenerator.onSuccess()
    }
}