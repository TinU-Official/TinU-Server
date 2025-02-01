package com.tinuproject.tinu.security.oauth2.handler

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class OAuthLoginFailureHandler(): SimpleUrlAuthenticationFailureHandler() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException
    ) {
        log.error("LOGIN FAILED : {}", exception.message)
        super.onAuthenticationFailure(request, response, exception)
    }
}