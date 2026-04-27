package com.tinuproject.tinu.infra.security.oauth.handler

import com.tinuproject.tinu.domain.member.entity.RefreshToken
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.member.repository.RefreshTokenRepository
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import com.tinuproject.tinu.infra.security.oauth.dto.CustomOAuth2User
import com.tinuproject.tinu.global.web.CookieGenerator
import com.tinuproject.tinu.infra.security.jwt.JwtProperties
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.Cookie
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

import java.util.*

@Component
class OAuthLoginSuccessHandler(

    private val jwtUtil: JwtUtil,

    private val refreshTokenRepository: RefreshTokenRepository,

    private val memberRepository: MemberRepository,

    @Value("\${jwt.redirect}")
    private val REDIRECT_URL: String,

    @Value("\${jwt.redirect.sign}")
    private val SIGN_REDIRECT_URL: String,

    @Value("\${cookie.token.refresh-token}")
    private val REFRESH_TOKEN_KEY: String,

    private val jwtProperties: JwtProperties,


    ) : SimpleUrlAuthenticationSuccessHandler() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @Throws
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ){
        val oauth2User: CustomOAuth2User = authentication.principal as CustomOAuth2User

        val userId : UUID = oauth2User.userInfoDto.uuid

        // 리프레쉬 토큰 발급 후 저장
        val refreshToken: String =  jwtUtil.generateRefreshToken()
        val newRefreshToken = RefreshToken(userId = userId, token = refreshToken)
        refreshTokenRepository.save(newRefreshToken)

        val existMember =memberRepository.existsByUserId(userId)

        val redirectUri = if(existMember){
            String.format(REDIRECT_URL)
        }else{
            String.format(SIGN_REDIRECT_URL)
        }

        //TODO(임시로 쿼리 파람으로 Token을 포함시켜서 응답)
        //TODO(이후 운영 이전 어떤 방식으로 할 지 추가 논의)
        val targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("token", refreshToken)
            .build().toUriString()

//        response?.addHeader(HttpHeaders.SET_COOKIE, CookieGenerator.createCookies(
//            key = REFRESH_TOKEN_KEY,
//            value =  refreshToken,
//            path =  "/api/token",
//            sameSite = Cookie.SameSite.NONE,
//            maxAge = jwtProperties.refreshToken.expirationTime/1000
//        ))
        response?.sendRedirect(targetUrl)
    }


}