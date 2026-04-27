package com.tinuproject.tinu.domain.member.controller

import com.tinuproject.tinu.global.response.dto.ResponseDTO
import com.tinuproject.tinu.domain.member.exception.ExpiredTokenException
import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import com.tinuproject.tinu.domain.member.exception.UnknownTokenException
import com.tinuproject.tinu.domain.member.service.dto.output.Tokens
import com.tinuproject.tinu.domain.member.service.RefreshTokenService
import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.infra.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.global.response.dto.NullResponse
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.global.web.RefreshTokenResolver
import com.tinuproject.tinu.infra.security.exception.auth.NeedLoginException
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/token")
class RefreshTokenController(
    private val refreshTokenService : RefreshTokenService,

    private val refreshTokenResolver: RefreshTokenResolver,

    @param:Value("\${jwt.header.access-token}")
    private val accessTokenHeaderName: String,

    @param:Value("\${jwt.header.refresh-token}")
    private val refreshTokenHeaderName : String,
) {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    @GetMapping("/reissue")
    @SwaggerExceptionResponses(exceptions = [NotFoundTokenException::class, InvalidedTokenException::class, ExpiredTokenException::class, ])
    @Operation(summary = "AccessToken 재발급 API", description = "RefreshToken을 통해 AccessToken을 재발급 받는 로직입니다.")
    fun refreshAccessToken(request : HttpServletRequest, response: HttpServletResponse): ResponseEntity<ResponseDTO<NullResponse?>> {
        log.info("AccessToken 갱신 시도")

        try{
            val refreshToken = refreshTokenResolver.resolve(request)

            val tokens : Tokens = refreshTokenService.reissueAccessTokenByRefreshToken(refreshToken)

            response.addHeader(accessTokenHeaderName, "Bearer ${tokens.accessToken}")

            response.addHeader(refreshTokenHeaderName, tokens.refreshToken)

            return ResponseEntityGenerator.onSuccess()
        }catch(e : NotFoundTokenException){
            log.warn("RefreshToken이 없습니다. 다시 로그인을 진행해야합니다.")
            throw NeedLoginException()
        //실제 MaxAge가 RefreshToken TTL 만큼이라 가능성은 적지만 방어적 코드
        }catch(e : ExpiredTokenException){
            log.warn("RefrshToken이 만료되었습니다. 다시 로그인을 진행해야합니다.")
            throw NeedLoginException()
        }catch(e : UnknownTokenException){
            log.warn("RefreshToken에 알지 못하는 문제가 발생하였습니다. 다시 로그인을 진행해야합니다.")
            throw NeedLoginException()
        }catch(e : BaseException){
            log.warn(("%s와 같은 문제가 발생하였습니다.").format(e.getResponse().toString()))
            throw NeedLoginException()
        }catch(e : Exception){
            log.warn("토큰 재발행과 관련하여 알지 못하는 오류가 발생하였습니다. 일단 다시 로그인을 진행해야합니다.")
            log.warn("Error message", e)
            throw NeedLoginException()
        }
    }
}