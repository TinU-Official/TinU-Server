package com.tinuproject.tinu.infra.security.oauth.apple

import com.tinuproject.tinu.infra.security.config.AppleProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.Date.*

@Component
class AppleJwtGenerator(
    val appleProperties: AppleProperties
) {

    fun generate(): String {
        val now = Instant.now()
        val exp = now.plus(180, ChronoUnit.DAYS)

        return Jwts.builder()
            .setHeaderParam("kid", appleProperties.loginKey)
            .setIssuer(appleProperties.teamId)
            .setIssuedAt(from(now))
            .setExpiration(from(exp))
            .setAudience("https://appleid.apple.com")
            .setSubject(appleProperties.clientId)
            .signWith(loadPrivateKey(), SignatureAlgorithm.RS256)
            .compact()
    }

    private fun loadPrivateKey(): PrivateKey {
        val privateKeyPem = appleProperties.clientSecret
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s+".toRegex(), "") // 공백/줄바꿈 제거

        val keyBytes = Base64.getDecoder().decode(privateKeyPem)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)

    }
}