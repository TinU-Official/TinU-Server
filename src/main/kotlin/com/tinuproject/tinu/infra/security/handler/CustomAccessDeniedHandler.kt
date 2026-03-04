package com.tinuproject.tinu.infra.security.handler


import com.fasterxml.jackson.databind.ObjectMapper
import com.tinuproject.tinu.domain.member.exception.NeedRegistException
import com.tinuproject.tinu.global.response.ResponseEntityGenerator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        // 1. "권한 없음"을 의미하는 커스텀 예외 생성
        // 현재는 회원가입을 마치지 않은 유저(GUEST)가 요청을 보낸 경우만 이기에 회원가입을 진행하라는 예외 반환
        val resultException = NeedRegistException()

        // 2. ResponseEntityGenerator를 통해 공통 응답 포맷 생성
        val responseEntity = ResponseEntityGenerator.onFailure(resultException)

        // 3. 응답 설정 (403 Forbidden)
        response.status = responseEntity.statusCode.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        // 4. JSON 변환 및 쓰기
        response.writer.write(objectMapper.writeValueAsString(responseEntity.body))
    }
}