package com.tinuproject.tinu

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun baseException(e : BaseException) : ResponseEntity<ResponseDTO>{
        return ResponseEntityGenerator.onFailure(e)
    }
}