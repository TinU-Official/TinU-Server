package com.tinuproject.tinu.global.web

import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import jakarta.servlet.http.HttpServletRequest

object AccessTokenResolver : TokenResolver {

    private const val ACCESS_TOKEN_HEADER_NAME = "Authorization"
    private const val BEARER_PREFIX = "Bearer "

    override fun resolve(request: HttpServletRequest): String {
        val header = request.getHeader(ACCESS_TOKEN_HEADER_NAME)
            ?: throw NotFoundTokenException()

        // prefix 체크와 토큰 추출을 동시에 처리
        return header.takeIf { it.startsWith(BEARER_PREFIX, ignoreCase = true) }
            ?.removePrefix(BEARER_PREFIX)
            ?.trim()
            ?: throw InvalidedTokenException()
    }
}