package com.tinuproject.tinu.global.web

import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RefreshTokenResolver(

    @param:Value("\${jwt.header.refresh-token}")
    private val refreshTokenHeaderName: String

) : TokenResolver{


    override fun resolve(request: HttpServletRequest): String {
        val header = request.getHeader(refreshTokenHeaderName)
            ?: throw NotFoundTokenException()

        return header
    }
}