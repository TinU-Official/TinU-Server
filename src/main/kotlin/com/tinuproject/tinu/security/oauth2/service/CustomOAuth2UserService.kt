package com.tinuproject.tinu.security.oauth2.service

import com.tinuproject.tinu.domain.entity.SocialMember
import com.tinuproject.tinu.domain.enums.Social
import com.tinuproject.tinu.domain.socialmember.repository.SocialMemberRepository
import com.tinuproject.tinu.domain.token.refreshtoken.repository.RefreshTokenRepository
import com.tinuproject.tinu.security.oauth2.dto.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOAuth2UserService(
    private val userRepository: SocialMemberRepository,
    private val refreshTokenRepository : RefreshTokenRepository
): DefaultOAuth2UserService() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    private var oAuth2UserInfo: OAuth2UserInfoDto? = null

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {

        val provider :String = userRequest!!.clientRegistration.clientName

        val oauth2User : OAuth2User = super.loadUser(userRequest)


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


}