package com.tinuproject.tinu.security.oauth2.handler

import com.tinuproject.tinu.domain.entity.RefreshToken
import com.tinuproject.tinu.domain.entity.SocialMember
import com.tinuproject.tinu.domain.enums.Social
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.socialmember.repository.SocialMemberRepository
import com.tinuproject.tinu.domain.token.refreshtoken.repository.RefreshTokenRepository
import com.tinuproject.tinu.security.jwt.JwtUtil
import com.tinuproject.tinu.security.oauth2.dto.CustomOAuth2User
import com.tinuproject.tinu.security.oauth2.dto.KakaoUserInfo
import com.tinuproject.tinu.security.oauth2.dto.NaverUserInfo
import com.tinuproject.tinu.security.oauth2.dto.OAuth2UserInfoDto
import com.tinuproject.tinu.web.CookieGenerator
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import java.net.URLEncoder
import java.util.*


@Component
class OAuthLoginSuccessHandler(

    private val jwtUtil: JwtUtil,

    private val refreshTokenRepository: RefreshTokenRepository,

    private val memberRepository: MemberRepository,

    @Value("\${jwt.redirect}")
    private val REDIRECT_URL : String,

    @Value("\${jwt.redirect.sign}")
    private val SIGN_REDIRECT_URL : String,

    @Value("\${jwt.access-token.expiration-time}")
    private val ACCESS_TOKEN_EXPIRATION_TIME: Long, // 액세스 토큰 유효기간



    @Value("\${jwt.refresh-token.expiration-time}")
    private val REFRESH_TOKEN_EXPIRATION_TIME: Long, // 리프레쉬 토큰 유효기간


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
        val refreshToken: String =  jwtUtil.generateRefreshToken(userId, REFRESH_TOKEN_EXPIRATION_TIME)
        val newRefreshToken = RefreshToken(userId = userId, token = refreshToken)
        refreshTokenRepository.save(newRefreshToken)

        // 액세스 토큰 발급
        val accessToken: String = jwtUtil.generateAccessToken(userId, ACCESS_TOKEN_EXPIRATION_TIME)

        val redirectUri = if(memberRepository.existsByUserId(userId)){
            String.format(REDIRECT_URL)
        }else{
            String.format(SIGN_REDIRECT_URL)
        }

        response?.addHeader(HttpHeaders.AUTHORIZATION,("Bearer $accessToken").toString())
        response?.addHeader(HttpHeaders.SET_COOKIE,CookieGenerator.createCookies("RefreshToken", refreshToken))
        response?.sendRedirect(redirectUri)
    }


}