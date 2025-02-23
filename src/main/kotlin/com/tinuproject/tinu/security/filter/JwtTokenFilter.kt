package com.tinuproject.tinu.security.filter

import com.tinuproject.tinu.domain.exception.token.ExpiredTokenException
import com.tinuproject.tinu.domain.exception.token.InvalidedTokenException
import com.tinuproject.tinu.domain.exception.token.NotFoundTokenException
import com.tinuproject.tinu.security.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import kotlin.collections.ArrayList


class JwtTokenFilter(

    val jwtUtil : JwtUtil,

    //토큰이 없어도 되는 api
    private val excludeUrls : List<String>

) : OncePerRequestFilter(){
    var log : Logger = LoggerFactory.getLogger(this::class.java);


    fun hasJwtToken(httpServeletRequest : HttpServletRequest) : String{

        val accessToken : String?= httpServeletRequest.getHeader(HttpHeaders.AUTHORIZATION)

        accessToken?: throw NotFoundTokenException()

        return accessToken
    }

    fun validateToken(accessToken : String){
        jwtUtil.validateToken(accessToken)
    }


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var httpServeletRequest : HttpServletRequest = request
        var accessToken : String

        try{
            hasJwtToken(httpServeletRequest)
            accessToken = jwtUtil.getTokenFromHeader(httpServeletRequest)
            validateToken(accessToken)
            val userId = jwtUtil.getUserIdFromToken(accessToken)
            val authentication = UsernamePasswordAuthenticationToken(UUID.fromString(userId), null, ArrayList())
            SecurityContextHolder.getContext().authentication = authentication
        }catch (e : NotFoundTokenException){
            log.warn("토큰이 없습니다.")
            throw e
        }catch (e : InvalidedTokenException){
            log.warn("토큰이 유효하지 않습니다.")
            throw e
        }catch (e : ExpiredTokenException){
            log.warn("토큰이 만료되었습니다.")
            throw e
        }


        filterChain.doFilter(request,response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}