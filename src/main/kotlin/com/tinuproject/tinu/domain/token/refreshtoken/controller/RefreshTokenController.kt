package com.tinuproject.tinu.domain.token.refreshtoken.controller

import com.tinuproject.tinu.domain.exception.base.ResponseDTO
import com.tinuproject.tinu.domain.exception.token.InvalidedTokenException
import com.tinuproject.tinu.domain.exception.token.NotFoundTokenException
import com.tinuproject.tinu.domain.token.Tokens
import com.tinuproject.tinu.domain.token.refreshtoken.service.RefreshTokenService
import com.tinuproject.tinu.web.CookieGenerator
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("api/token")
class RefreshTokenController(
    private val refreshTokenService : RefreshTokenService,

    @Value("\${cookie.token.access-token}")
    private val accessTokenKey : String,

    @Value("\${cookie.token.refresh-token}")
    private val refreshTokenkey : String
) {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    @GetMapping("/generate")
    fun generateAccessToken(httpServletResponse: HttpServletResponse, @CookieValue(name = "RefreshToken") refreshToken : String?): ResponseEntity<ResponseDTO> {
        var tokens : Tokens
        log.info("AccessToken 갱신 시도")
        if(refreshToken==null){
            throw NotFoundTokenException()
        }
        try{
            tokens = refreshTokenService.ReissueAccessTokenByRefreshToken(refreshToken)
        }catch (e : InvalidedTokenException){
            throw e
        }

        httpServletResponse.addCookie(CookieGenerator.createCookies(accessTokenKey,tokens.accessToken))
        httpServletResponse.addCookie(CookieGenerator.createCookies(refreshTokenkey, tokens.refreshToken))
        var body : MutableMap<String, Any> = mutableMapOf()

        body.put("Tokens",tokens)

        val result = ResponseDTO(
            isSucess = true,
            httpStatusCode = HttpStatus.OK.ordinal,
            result = body
        )

        var responseEntity : ResponseEntity<ResponseDTO> = ResponseEntity.ok().body(result)
        return responseEntity
    }
}