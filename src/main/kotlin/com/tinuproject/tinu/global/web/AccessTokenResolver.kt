package com.tinuproject.tinu.global.web

import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AccessTokenResolver(
    @param:Value("\${jwt.header.access-token}")
    private val accessTokenHeaderName :String

) : TokenResolver {

    override fun resolve(request: HttpServletRequest): String {
        val header = request.getHeader(accessTokenHeaderName)
            ?: throw NotFoundTokenException()

        // prefix 체크와 토큰 추출을 동시에 처리
        return header.takeIf { it.startsWith(BEARER_PREFIX, ignoreCase = true) }
            ?.removePrefix(BEARER_PREFIX)
            ?.trim()
            ?: throw InvalidedTokenException()
    }

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }
}