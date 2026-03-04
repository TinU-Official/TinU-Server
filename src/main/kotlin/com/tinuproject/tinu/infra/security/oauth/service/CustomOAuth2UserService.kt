package com.tinuproject.tinu.infra.security.oauth.service

import com.tinuproject.tinu.infra.security.oauth.dto.*
import com.tinuproject.tinu.domain.member.entity.SocialMember
import com.tinuproject.tinu.domain.member.enums.Social
import com.tinuproject.tinu.domain.member.repository.SocialMemberRepository
import com.tinuproject.tinu.domain.member.repository.RefreshTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomOAuth2UserService(
    private val userRepository: SocialMemberRepository,
    private val refreshTokenRepository : RefreshTokenRepository,
    private val appleOAuth2UserService: AppleOAuth2UserService
): DefaultOAuth2UserService() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)


    private var oAuth2UserInfo: OAuth2UserInfoDto? = null

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {

        val provider :String = userRequest!!.clientRegistration.clientName
        //Apple은 사용자 정보를 id_token 안에 JWT 형태로만 제공해서 Security에서 기본으로 loadUser
        // 기능으로 처리가 불가능하여 별도로 구현 및 분기처리를 해줘야함
        val oauth2User : OAuth2User = if(provider == "Apple"){
            appleOAuth2UserService.appleLoadUser(userRequest)
        }else{
            super.loadUser(userRequest)
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
                log.info("애플 로그인 요청")
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


}