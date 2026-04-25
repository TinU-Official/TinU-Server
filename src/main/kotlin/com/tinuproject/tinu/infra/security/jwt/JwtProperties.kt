package com.tinuproject.tinu.infra.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val accessToken: TokenProperties,
    val refreshToken: TokenProperties
) {
    data class TokenProperties(
        val secret: String,
        val expirationTime: Long
    )
}