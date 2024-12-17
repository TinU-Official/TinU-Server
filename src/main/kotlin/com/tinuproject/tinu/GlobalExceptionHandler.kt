package com.tinuproject.tinu

import com.fasterxml.jackson.databind.ser.Serializers.Base
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ResponseDTO
import com.tinuproject.tinu.domain.exception.token.InvalidedTokenException
import com.tinuproject.tinu.domain.exception.token.NotFoundTokenException
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    //TODO 이걸로도 정상 작동하는지 테스트 아마 될 것으로 예상
    @ExceptionHandler(BaseException::class)
    fun baseException(e : BaseException) : ResponseEntity<ResponseDTO>{
        return ResponseEntityGenerator.onFailure(e)
    }
}