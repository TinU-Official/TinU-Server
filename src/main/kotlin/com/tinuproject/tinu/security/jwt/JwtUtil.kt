package com.tinuproject.tinu.security.jwt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.SignatureException
import java.util.*
import javax.crypto.SecretKey
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys


@Component
@PropertySource("classpath:/secret.yml")
class JwtUtil {
    var log : Logger = LoggerFactory.getLogger(this::class.java)
    @Value("\${jwt.secret}")
    lateinit var SECRET_KEY : String

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    // 액세스 토큰을 발급하는 메서드
    fun generateAccessToken(uuid: UUID, expirationMillis: Long): String {
        log.info("액세스 토큰 발행.")
        return Jwts.builder()
            .claim("userId", uuid.toString()) // 클레임에 userId 추가
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey())
            .compact()
    }

    // 리프레쉬 토큰을 발급하는 메서드
    fun generateRefreshToken(uuid : UUID, expirationMillis: Long): String {
        log.info("리프레쉬 토큰 발행.")
        return Jwts.builder()
            .claim("userId", uuid.toString()) // 클레임에 userId 추가
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMillis))
            .signWith(getSigningKey())
            .compact()
    }

    // 응답 헤더에서 액세스 토큰을 반환하는 메서드
    fun getTokenFromHeader(authorizationHeader: String): String {
        //hasText(token)토큰이 넘어왔는지 확인
        if(StringUtils.hasText(authorizationHeader))
            return authorizationHeader.substring(0)
        else
            //TODO("이후 토큰이 없는 경우 반환하는 Exception을 만들어서 처리.")
            throw Exception()
    }

    // 토큰에서 유저 id를 반환하는 메서드
    fun getUserIdFromToken(token: String?): String {
        return try {
            val userId: String = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body
                .get("userId", String::class.java)
            log.info("유저 id 반환")
            userId
        } catch (e: JwtException) {
            // 토큰이 유효하지 않은 경우
            log.warn("유효하지 않은 토큰입니다.")
            //(토큰이 유효하지 않는 경우 반환하는 Exception을 만들어 처리)
            throw Exception()
        } catch (e: IllegalArgumentException) {
            log.warn("유효하지 않은 토큰입니다.")
            //(토큰이 유효하지 않는 경우 반환하는 Exception을 만들어 처리)
            throw Exception()
        }
    }

    //Token 에서 claim 반환하는 메서드
    fun getClaimsFromToken(token : String?) : Claims{
        try{

            return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body
        }catch(e : SignatureException){
            log.warn("Claim이 유효하지 않은 토큰입니다.{}",e.message)
            //TODO(유효하지 않는 토큰 처리")
            throw Exception()
        }catch (e : Exception){
            log.warn("토큰과 관련한 예기치 못한 에러가 발생했습니다.")
            //TODO(예상치 못한 오류 에러로 처리)
            throw Exception()
        }
    }

    //토큰이 유효한지 확인
    fun validateToken(token : String){
        getClaimsFromToken(token);
    }

    //토큰 속 유저와 실제 기대하는 user의 비교 검증
    //ex : 글 수정 요청이 들어왔을때 실제 글쓴이와, 요구하는 Token 속 유저의 정보 비교
    fun validateUserFromToken(token :String, expectedUserId : UUID){
        validateToken(token);
        if(!getUserIdFromToken(token).equals(expectedUserId.toString())){
            //TODO(요청이 들어왔는데 해당 요청이 가능한 유저와 실제 요청 유저가 다를때 반환하는 에러로 처리)
            //권한 없음
            throw Exception()
        }
    }

    fun isExpired(token : String) : Boolean{
        return getClaimsFromToken(token).expiration.before(Date())
    }
}