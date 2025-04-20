package com.tinuproject.tinu.infra.security.oauth.handler

import com.tinuproject.tinu.domain.member.entity.RefreshToken
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.member.repository.RefreshTokenRepository
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import com.tinuproject.tinu.infra.security.oauth.dto.CustomOAuth2User
import com.tinuproject.tinu.global.web.CookieGenerator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

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

        val existMember =memberRepository.existsByUserId(userId)
        // 액세스 토큰 발급
        //Todo(이후 삭제 예정 - 로그인 진행 이후 별도의 재발급 요청으로 A.T를 받아올 수 밖에 없어서 안쓰는 로직이지만 테스트 간 A.T를 쉽게 구하기 위해 남겨둠.)
        val accessToken: String = jwtUtil.generateAccessToken(userId, ACCESS_TOKEN_EXPIRATION_TIME,existMember)

        val redirectUri = if(existMember){
            String.format(REDIRECT_URL)
        }else{
            String.format(SIGN_REDIRECT_URL)
        }
//        log.info("accessToken : $accessToken")
//        log.info("refreshToken : $refreshToken")
        response?.addHeader(HttpHeaders.AUTHORIZATION,("Bearer $accessToken").toString())
        response?.addHeader(HttpHeaders.SET_COOKIE, CookieGenerator.createCookies("RefreshToken", refreshToken))
        response?.sendRedirect(redirectUri)
    }


}