package com.tinuproject.tinu.domain.exception.base

import com.tinuproject.tinu.DTO.ErrorResponse
import com.tinuproject.tinu.DTO.ResponseDTO
import java.lang.RuntimeException

open class BaseException(
    protected val errorCode : ErrorCode
): RuntimeException(), BaseErrorCode {

    override fun getResponse(): ResponseDTO<ErrorResponse> {
        return ResponseDTO<ErrorResponse>(isSuccess = false, stateCode = errorCode.httpStatusCode, result = ErrorResponse(message=errorCode.message, statusCode = errorCode.stateCode))
    }
}