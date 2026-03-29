package com.tinuproject.tinu.infra.security.filter

import com.tinuproject.tinu.domain.member.exception.ExpiredTokenException
import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.web.AccessTokenResolver
import com.tinuproject.tinu.infra.security.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*


class JwtTokenFilter(

    val jwtUtil : JwtUtil,

    val accessTokenResolver: AccessTokenResolver

) : OncePerRequestFilter(){
    var log : Logger = LoggerFactory.getLogger(this::class.java);

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // 1. 토큰 추출 (여기서 토큰이 없어서 에러가 나거나 null이면 catch로 빠짐)
            val accessToken: String = accessTokenResolver.resolve(request)

            // 2. 토큰 파싱 및 검증 (만료, 위조 시 예외 발생 -> catch 이동)
            val claims = jwtUtil.getClaimsFromAccessToken(accessToken)

            // 3. 사용자 정보 추출
            val userId = jwtUtil.getUserIdFromClaims(claims)

            log.info(userId)

            val isSigned = jwtUtil.isSigned(claims)

            // 4. 가입 여부에 따른 권한(Role) 분기 처리
            val authorities = if (isSigned) {
                Collections.singletonList(SimpleGrantedAuthority("ROLE_USER")) // 정회원
            } else {
                log.info("ROLE_GUEST 추가")
                Collections.singletonList(SimpleGrantedAuthority("ROLE_GUEST")) // 소셜로그인만 한 상태
            }

            // 5. 인증 객체 생성 및 SecurityContext 저장
            val authentication = UsernamePasswordAuthenticationToken(
                UUID.fromString(userId),
                null,
                authorities
            )

            SecurityContextHolder.getContext().authentication = authentication

        } catch (e: ExpiredTokenException) {
            // 토큰 만료 시: 예외를 던지지 않고 request에 속성 저장 (나중에 EntryPoint가 확인)
            log.warn( " 토큰이 만료되었습니다: {} " , e.message, e )
            log.warn(e.message)
            request.setAttribute("exception", e)
        } catch (e: NotFoundTokenException){
            log.warn(e.message)
            request.setAttribute("exception", e)
        }
        catch (e: BaseException) {
            // 토큰 위조, 없음 등 기타 예외
            log.warn(e.stackTrace.toString())
            log.warn("토큰이 이상합니다.")
            request.setAttribute("exception", InvalidedTokenException())
        } catch (e: Exception) {
            // 예상치 못한 예외
            log.warn(e.stackTrace.toString())
            log.warn("예상치 못한 예외가 발생했습니다.")
            log.error("JwtFilter Error: {}", e.message)
            request.setAttribute("exception", InvalidedTokenException())
        }

        // 6. 다음 필터로 진행 (인증이 되었든, 예외가 발생했든 일단 넘김)
        // -> 인증 실패 상태라면 SecurityConfig가 감지하고 EntryPoint를 호출함
        filterChain.doFilter(request, response)
    }
}