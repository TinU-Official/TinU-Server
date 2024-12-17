package com.tinuproject.tinu.security.oauth2.dto

class KakaoUserInfo(
    private var attributes: Map<String, Any>
) :OAuth2UserInfoDto {
    override fun getProviderId(): String {
        return attributes["id"].toString()
    }

    override fun getProvider(): String {
        return "kakao"
    }

    override fun getName() :String{

        return (attributes["properties"] as Map<*, *>)["nickname"].toString()

    }
}