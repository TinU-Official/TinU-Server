package com.tinuproject.tinu.security.filter

import com.tinuproject.tinu.security.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource


@PropertySource("classpath:/secret.yml")
class JwtTokenFilter(
    val jwtUtil : JwtUtil,

    ) : GenericFilter(){
    var log : Logger = LoggerFactory.getLogger(this::class.java);
    @Value("\${token.access-token}")
    lateinit var ACCESSTOKEN_COOKIE : String;

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        var httpServeletRequest : HttpServletRequest = request as HttpServletRequest
        var accessToken : String
        try{
            accessToken = hasJwtToken(httpServeletRequest)
            validateToken(accessToken)

        }catch (e : Exception){//TODO(토큰이 없음을 나타내는 Exception을 매개변수로 설정) - 토큰이 없음.
            throw e
        }catch (e : Exception){//TODO(토큰이 유효하지 않음을 나타내는 Exception을 매개변수로 설정) - 토큰은 있지만 잘못된 토큰
            throw e
        }catch (e : Exception){//TODO(토큰이 만료되었음을 나타내는 Exception을 매개변수로 설정) - 토큰이 있지만 만료됨.
            throw e
        }

        chain!!.doFilter(request,response)
    }


    fun hasJwtToken(httpServeletRequest : HttpServletRequest) : String{
        val cookies = httpServeletRequest.cookies
        var hasToken : Boolean = false
        var accesToken : String = ""
        if(cookies==null) throw Exception()

        for(cookie in cookies){
            //Cookie들 중 AccessToken을 갖고 있는지
            if(cookie.name.equals(ACCESSTOKEN_COOKIE)){
                hasToken = true
                accesToken = cookie.value
            }
        }
        //AccessToken을 갖고 있지 않음.
        if(!hasToken){
            //TODO(토큰이 없음을 나타내는 Exception 반환)
            throw Exception()
        }

        return accesToken
    }

    fun validateToken(accessToken : String){
        jwtUtil.validateToken(accessToken)
    }

    fun expireToken(accessToken: String){
        if(jwtUtil.isExpired(accessToken)){
            //TODO(토큰이 만료되었음을 나타내는 Exception 반환)
            throw Exception()
        }
    }

}