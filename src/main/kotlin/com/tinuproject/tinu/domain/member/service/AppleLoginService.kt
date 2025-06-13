package com.tinuproject.tinu.domain.member.service

interface AppleLoginService {


    /**
     * Apple 소셜로그인 Uri 반환 메서드
     */
    fun getAppleLoginUri() : String


    fun createClientSecret() : String

    fun getPrivateKey() : String

}