package com.tinuproject.tinu.security.filter

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.token.ExpiredTokenException
import com.tinuproject.tinu.domain.exception.token.InvalidedTokenException
import com.tinuproject.tinu.domain.exception.token.NotFoundTokenException
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
import org.springframework.stereotype.Component



class JwtTokenFilter(

    val jwtUtil : JwtUtil,

    private val ACCESSTOKEN_COOKIE : String

) : GenericFilter(){
    var log : Logger = LoggerFactory.getLogger(this::class.java);


    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        var httpServeletRequest : HttpServletRequest = request as HttpServletRequest
        var accessToken : String

        val requestUrl = httpServeletRequest.requestURL
        log.info(requestUrl.toString())


        try{
            accessToken = hasJwtToken(httpServeletRequest)
            validateToken(accessToken)
            expireToken(accessToken)
        }catch (e : NotFoundTokenException){//TODO(토큰이 없음을 나타내는 Exception을 매개변수로 설정) - 토큰이 없음.
            log.warn("토큰이 없습니다.")
            throw e
        }catch (e : InvalidedTokenException){//TODO(토큰이 유효하지 않음을 나타내는 Exception을 매개변수로 설정) - 토큰은 있지만 잘못된 토큰
            log.warn("토큰이 유효하지 않습니다.")
            throw e
        }catch (e : ExpiredTokenException){//TODO(토큰이 만료되었음을 나타내는 Exception을 매개변수로 설정) - 토큰이 있지만 만료됨.
            log.warn("토큰이 만료되었습니다.")
            throw e
        }
        /*
            위 3개의 catch문을
            catch(e:BaseException){
                log.warn(e.message)
                throw e
            }
            로 대체 가능
        */

        chain!!.doFilter(request,response)
    }


    fun hasJwtToken(httpServeletRequest : HttpServletRequest) : String{
        val cookies = httpServeletRequest.cookies
        var hasToken : Boolean = false
        var accesToken : String= ""
        if(cookies==null) throw NotFoundTokenException()

        for(cookie in cookies){
            //Cookie들 중 AccessToken을 갖고 있는지
            if(cookie.name.equals(ACCESSTOKEN_COOKIE)){
                hasToken = true
                accesToken = cookie.value
            }
        }
        //AccessToken을 갖고 있지 않음.
        if(!hasToken){
            //TODO(토큰이 없음을 나타내는 Exception 반환) - 해결
            throw NotFoundTokenException()
        }

        return accesToken
    }

    fun validateToken(accessToken : String){

        jwtUtil.validateToken(accessToken)

    }

    fun expireToken(accessToken: String){
        if(jwtUtil.isExpired(accessToken)){
            //TODO(토큰이 만료되었음을 나타내는 Exception 반환) - 해결
            throw ExpiredTokenException()
        }
    }

}