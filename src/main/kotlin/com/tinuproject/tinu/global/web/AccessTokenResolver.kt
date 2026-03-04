package com.tinuproject.tinu.global.web

import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import jakarta.servlet.http.HttpServletRequest

object AccessTokenResolver : TokenResolver {

    private const val ACCESS_TOKEN_COOKIE_NAME = "access-token"

    override fun resolve(request: HttpServletRequest): String {
        val cookies = request.cookies ?: throw NotFoundTokenException()

        return cookies
            .firstOrNull { it.name == ACCESS_TOKEN_COOKIE_NAME }
            ?.value
            ?: throw NotFoundTokenException()
    }
}