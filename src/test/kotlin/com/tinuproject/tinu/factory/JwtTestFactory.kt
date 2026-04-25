package com.tinuproject.tinu.factory

import com.tinuproject.tinu.infra.security.jwt.JwtProperties
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTestFactory(

    private val jwtProperties: JwtProperties,

    var jwtUtil: JwtUtil
) {

    private fun getSigningKey(SECRET_KEY : String): SecretKey {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    // 액세스 토큰을 발급하는 메서드
    fun generateInvalidSecretKey(uuid: UUID, expirationMillis: Long, isSign : Boolean): String {
        return Jwts.builder()
            .claim("userId", uuid.toString())// 클레임에 userId 추가
            .claim("isSign", isSign)// 클레임에 회원가입 여부 추가.
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey("testsecrettestsecrettestsecrettesttestsecrettestsecrettestsecrettest"))
            .compact()
    }

    fun generateInvalidUserID( expirationMillis: Long, isSign : Boolean) : String{
        return Jwts.builder()
            .claim("isSign", isSign)// 클레임에 회원가입 여부 추가.
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey(jwtProperties.accessToken.secret))
            .compact()
    }

    fun generateInvalidIsSign(uuid:UUID, expirationMillis: Long) : String{
        return Jwts.builder()
            .claim("userId", uuid.toString())// 클레임에 userId 추가
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey(jwtProperties.accessToken.secret))
            .compact()
    }

    fun generateAccessToken(isSign: Boolean) : String{
        return jwtUtil.generateAccessToken(UUID.randomUUID(), 3600000, isSign)
    }
}