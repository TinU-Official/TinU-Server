package com.tinuproject.tinu.security.oauth2.handler

import com.tinuproject.tinu.domain.entity.RefreshToken
import com.tinuproject.tinu.domain.entity.SocialMember
import com.tinuproject.tinu.domain.enum.Social
import com.tinuproject.tinu.domain.socialmember.repository.SocialMemberRepository
import com.tinuproject.tinu.security.RefreshTokenRepository
import com.tinuproject.tinu.security.jwt.JwtUtil
import com.tinuproject.tinu.security.oauth2.dto.KakaoUserInfo
import com.tinuproject.tinu.security.oauth2.dto.NaverUserInfo
import com.tinuproject.tinu.security.oauth2.dto.OAuth2UserInfoDto
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import java.net.URLEncoder
import java.util.*


@Component
@PropertySource("classpath:/secret.yml")
class OAuthLoginSuccessHandler(

    private val jwtUtil: JwtUtil,

    private val userRepository: SocialMemberRepository ,

    private val refreshTokenRepository: RefreshTokenRepository,

    @Value("\${jwt.redirect}")
    private val REDIRECT_URL : String,

    @Value("\${jwt.access-token.expiration-time}")
    private val ACCESS_TOKEN_EXPIRATION_TIME: Long, // 액세스 토큰 유효기간



    @Value("\${jwt.refresh-token.expiration-time}")
    private val REFRESH_TOKEN_EXPIRATION_TIME: Long // 리프레쉬 토큰 유효기간


) : SimpleUrlAuthenticationSuccessHandler() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)



    private var oAuth2UserInfo: OAuth2UserInfoDto? = null

    @Throws
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ){
        val token: OAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken // 토큰
        val provider: String = token.getAuthorizedClientRegistrationId() // provider 추출
        when (provider) {

            "kakao" -> {
                log.info("카카오 로그인 요청")
                oAuth2UserInfo = KakaoUserInfo(token.getPrincipal().getAttributes())
            }

            "naver" -> {
                log.info("네이버 로그인 요청")
                oAuth2UserInfo =
                    NaverUserInfo(token.getPrincipal().getAttributes().get("response") as Map<String, Any>)
            }
        }

        // 정보 추출
        val providerId = oAuth2UserInfo!!.getProviderId()
        val name = oAuth2UserInfo!!.getName()
        val existUser: SocialMember? = userRepository. findByProviderId(providerId)
        val user: SocialMember
        if (existUser == null) {
            // 신규 유저인 경우
            log.info("신규 유저입니다. 등록을 진행합니다.")
            user = SocialMember(userId = UUID.randomUUID(),provider= Social.getSocial(provider), providerId = providerId)
            userRepository.save(user)
        } else {
            // 기존 유저인 경우
            log.info("기존 유저입니다.")
            refreshTokenRepository.deleteByUserId(existUser.userId)
            user = existUser
        }
        log.info("유저 이름 : {}", name)
        log.info("PROVIDER : {}", provider)
        log.info("PROVIDER_ID : {}", providerId)

        // 리프레쉬 토큰 발급 후 저장
        val refreshToken: String =  jwtUtil.generateRefreshToken(user.userId, REFRESH_TOKEN_EXPIRATION_TIME)
        val newRefreshToken: RefreshToken = RefreshToken(userId = user.userId, token = refreshToken)
        refreshTokenRepository.save(newRefreshToken)

        // 액세스 토큰 발급
        var accessToken: String = jwtUtil.generateAccessToken(user.userId, ACCESS_TOKEN_EXPIRATION_TIME)
//        accessToken = "Bearer "+accessToken
        // 이름, 액세스 토큰, 리프레쉬 토큰을 담아 리다이렉트
        val encodedName: String = URLEncoder.encode(name, "UTF-8")
        val redirectUri = String.format(REDIRECT_URL, encodedName, accessToken, "done")


        response?.addHeader("Authorization", accessToken)
        response?.addCookie(createCookies("AccessToken",accessToken))
        response?.addCookie(createCookies("RefreshToken", refreshToken))
        response?.sendRedirect(redirectUri)
    }

    fun createCookies(key : String, value : String) : Cookie {
        val cookie = Cookie(key, value)
        cookie.path = "/"
        cookie.isHttpOnly = false
        cookie.secure = false
        cookie.maxAge = 3600

        return cookie
    }


}