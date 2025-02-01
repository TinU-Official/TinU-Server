package com.tinuproject.tinu.security.oauth2.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    var userInfoDto : UserInfoDto
):OAuth2User {
    override fun getName(): String {
        return userInfoDto.name
    }

    override fun getAttributes(): MutableMap<String, Any>? {
        return null
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return ArrayList()
    }

}