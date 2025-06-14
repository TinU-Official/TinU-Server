package com.tinuproject.tinu.infra.security.oauth.dto

class AppleUserInfo(
    private var attributes: Map<String, Any>
) : OAuth2UserInfoDto {
    override fun getProviderId(): String {
        return attributes["sub"] as String
    }

    override fun getProvider(): String {
        return "apple"
    }

    override fun getName(): String {
        TODO("삭제 예정")
    }
}