package com.tinuproject.tinu.infra.security.oauth.service

import com.tinuproject.tinu.infra.security.oauth.dto.*
import com.tinuproject.tinu.domain.member.entity.SocialMember
import com.tinuproject.tinu.domain.member.enums.Social
import com.tinuproject.tinu.domain.member.repository.SocialMemberRepository
import com.tinuproject.tinu.domain.member.repository.RefreshTokenRepository
import com.tinuproject.tinu.infra.security.config.AppleProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class CustomOAuth2UserService(
    private val userRepository: SocialMemberRepository,
    private val refreshTokenRepository : RefreshTokenRepository,
    private val appleProperties: AppleProperties
): DefaultOAuth2UserService() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    private var oAuth2UserInfo: OAuth2UserInfoDto? = null

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {

        val provider :String = userRequest!!.clientRegistration.clientName

        val oauth2User : OAuth2User
        if(provider=="Apple"){
            oauth2User = appleLoadUser(userRequest)
        }else{
            oauth2User  = super.loadUser(userRequest)
        }

        when (provider) {

            "Kakao" -> {
                log.info("카카오 로그인 요청")
                oAuth2UserInfo = KakaoUserInfo(oauth2User.attributes)
            }

            "Naver" -> {
                log.info("네이버 로그인 요청")
                oAuth2UserInfo =
                    NaverUserInfo(oauth2User.attributes["response"] as Map<String, Any>)
            }

            "Google" -> {
                log.info("구글 로그인 요청")
                oAuth2UserInfo = GoogleUserInfo(oauth2User.attributes)
            }

            "Apple" -> {
                log.info("Apple 로그인 요청")
                oAuth2UserInfo = AppleUserInfo(oauth2User.attributes)
            }
        }

        val providerId = oAuth2UserInfo!!.getProviderId()
        val name = oAuth2UserInfo!!.getName()
        val existUser: SocialMember? = userRepository.findByProviderId(providerId)
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
        val userInfoDto = UserInfoDto(uuid = user.userId, name = name, providerId = providerId, provider = provider )

        log.info("유저 이름 : {}", name)
        log.info("PROVIDER : {}", provider)
        log.info("PROVIDER_ID : {}", providerId)
        log.info("USER_ID : {}",user.userId)

        return CustomOAuth2User(userInfoDto = userInfoDto)
    }

    fun appleLoadUser(userRequest: OAuth2UserRequest?) : OAuth2User{
        val idToken = userRequest!!.additionalParameters["id_token"] as? String
            ?: throw IllegalArgumentException("Missing id_token from Apple")

        val claims = parseIdToken(idToken)

        val attributes = mapOf(
            "sub" to claims["sub"],
            "email" to claims["email"],
            "email_verified" to claims["email_verified"]
        )

        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority("ROLE_USER")),
            attributes,
            "sub" // user-name-attribute
        )
    }

    private fun parseIdToken(idToken: String): Map<String, Any> {
        val secretKeyBytes = appleProperties.clientSecret.toByteArray(StandardCharsets.UTF_8)
        val key = Keys.hmacShaKeyFor(secretKeyBytes)


        val parser = Jwts.parserBuilder().setSigningKey(key).build()
        val jwt = parser.parseClaimsJws(idToken) // 서명 검증은 생략하거나 키로 구성 가능
        return jwt.body
    }


}