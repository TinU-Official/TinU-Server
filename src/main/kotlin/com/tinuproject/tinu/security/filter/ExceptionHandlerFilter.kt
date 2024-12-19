package com.tinuproject.tinu.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ResponseDTO
import com.tinuproject.tinu.web.ResponseEntityGenerator
import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.juli.logging.LogFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


class ExceptionHandlerFilter(
    private val objectMapper: ObjectMapper
): OncePerRequestFilter() {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    )  {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BaseException) {
            //토큰 문제
            setErrorResponse(response, e)
        }
    }


    fun setErrorResponse(response: ServletResponse?, e : BaseException){
        if(response is HttpServletResponse){
            response.status = e.getResponse().httpStatusCode!!
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            val result : ResponseDTO? = ResponseEntityGenerator.onFailure(e).body
            try {
                log.info(objectMapper.writeValueAsString(result))
                response.writer.write(objectMapper.writeValueAsString(result))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}