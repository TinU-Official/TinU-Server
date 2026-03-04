package com.tinuproject.tinu.infra.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.tinuproject.tinu.domain.member.exception.ExpiredTokenException
import com.tinuproject.tinu.domain.member.exception.InvalidedTokenException
import com.tinuproject.tinu.domain.member.exception.NotFoundTokenException
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import com.tinuproject.tinu.infra.security.exception.auth.NeedLoginException
import com.tinuproject.tinu.infra.security.exception.auth.NeedReissueTokenException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // 1. JwtTokenFilter에서 넘어온 예외 확인
        val exception = request.getAttribute("exception")

        // 2. 예외에 따른 에러 코드 선택
        // 현재 방식으로는 모든 요청에 대한 R.T 확인이 불가능하여 A.T에 문제가 있다면 
        // R.T로 일단 ReIssue 해보는 수 밖에 없어 모든 Excepton에 대해 NeedReIssueTokenException() 전송
        val resultException = when (exception) {
            is ExpiredTokenException -> NeedReissueTokenException()
            is NotFoundTokenException -> NeedReissueTokenException()
            is InvalidedTokenException -> NeedReissueTokenException()
            else -> NeedReissueTokenException()
        }

        // 3. ResponseEntityGenerator를 통해 공통 응답 포맷 생성
        val responseEntity = ResponseEntityGenerator.onFailure(resultException)

        // 4. 응답 설정
        response.status = responseEntity.statusCode.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        // 5. JSON 변환 및 쓰기 (ResponseEntity의 Body를 JSON 문자열로 변환)
        response.writer.write(objectMapper.writeValueAsString(responseEntity.body))
    }
}