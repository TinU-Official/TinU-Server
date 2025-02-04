package com.tinuproject.tinu.domain.exception.base

import com.tinuproject.tinu.DTO.ResponseDTO
import java.lang.RuntimeException

open class BaseException(
    protected val errorCode : ErrorCode
): RuntimeException(), BaseErrorCode {

    override fun getResponse(): ResponseDTO {
        val map : MutableMap<String, Any> = mutableMapOf()
        map["message"] = errorCode.message
        if(errorCode.stateCode!=null) map["errorCode"] = errorCode.stateCode
        return ResponseDTO(isSuccess = false, stateCode = errorCode.httpStatusCode, result = map)
    }
}