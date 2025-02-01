package com.tinuproject.tinu.security.oauth2.dto

interface OAuth2UserInfoDto {
    fun getProviderId() : String

    fun getProvider() : String

    fun getName() : String
}