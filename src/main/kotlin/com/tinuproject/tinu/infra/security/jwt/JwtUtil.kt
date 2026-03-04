package com.tinuproject.tinu.infra.security.jwt

import com.tinuproject.tinu.domain.member.exception.ExpiredTokenException
import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.UnknownTokenException
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.security.SignatureException
import java.util.*
import javax.crypto.SecretKey
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.jetbrains.annotations.TestOnly


@Component
class JwtUtil(

    private val jwtProperties: JwtProperties,

) {
    private val log = LoggerFactory.getLogger(this::class.java)

    private fun getSigningKey(secretKey: String): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    /* =======================
       Token Generate
       ======================= */

    fun generateAccessToken(
        uuid: UUID,
        isSign: Boolean
    ): String {
        log.info("액세스 토큰 발행")

        val props = jwtProperties.accessToken

        return Jwts.builder()
            .claim("userId", uuid.toString())
            .claim("isSign", isSign)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + props.expirationTime))
            .signWith(getSigningKey(props.secret))
            .compact()
    }

    @TestOnly
    fun generateAccessToken(
        uuid: UUID,
        expirationMillis : Long,
        isSign: Boolean
    ) : String{
        log.info("액세스 토큰 발행")

        val props = jwtProperties.accessToken

        return Jwts.builder()
            .claim("userId", uuid.toString())
            .claim("isSign", isSign)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey(props.secret))
            .compact()
    }

    fun generateRefreshToken(): String {
        log.info("리프레시 토큰 발행")

        val props = jwtProperties.refreshToken

        return Jwts.builder()
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + props.expirationTime))
            .signWith(getSigningKey(props.secret))
            .compact()
    }

    /* =======================
       Token Validate
       ======================= */

    private fun validateToken(
        token: String,
        secretKey: SecretKey
    ): Jws<Claims> {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

        } catch (e: SignatureException) {
            log.info("토큰 시그니처가 유효하지 않습니다.")
            throw InvalidedTokenException()

        } catch (e: ExpiredJwtException) {
            log.info("토큰이 만료되었습니다.")
            throw ExpiredTokenException()

        } catch (e: JwtException) {
            log.info("토큰에 예기치 못한 문제가 존재합니다.")
            throw UnknownTokenException()
        }
    }

    fun parseAccessToken(token: String): Jws<Claims> =
        validateToken(
            token,
            getSigningKey(jwtProperties.accessToken.secret)
        )

    fun parseRefreshToken(token: String): Jws<Claims> =
        validateToken(
            token,
            getSigningKey(jwtProperties.refreshToken.secret)
        )

    /* =======================
       Claims
       ======================= */

    fun getClaimsFromAccessToken(token: String): Claims =
        parseAccessToken(token).body

    fun getUserIdFromClaims(claims: Claims): String =
        claims["userId"]?.toString()
            ?: throw UnknownTokenException()

    fun isSigned(claims: Claims): Boolean =
        claims["isSign"]?.toString()?.toBoolean()
            ?: throw UnknownTokenException()
}