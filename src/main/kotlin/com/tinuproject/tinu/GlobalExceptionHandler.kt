package com.tinuproject.tinu

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.common.NotFoundException
import com.tinuproject.tinu.web.ResponseEntityGenerator
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun baseException(e : BaseException) : ResponseEntity<ResponseDTO>{
        return ResponseEntityGenerator.onFailure(e)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun noHandlerFoundException(e : NoHandlerFoundException) : ResponseEntity<ResponseDTO>{
        return ResponseEntityGenerator.onFailure(NotFoundException())
    }
}