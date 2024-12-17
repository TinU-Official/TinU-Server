package com.tinuproject.tinu.domain.exception.base

import com.fasterxml.jackson.databind.ser.Serializers.Base
import com.tinuproject.tinu.domain.exception.token.TokenErrorCode
import java.lang.RuntimeException

open class BaseException(
    protected val tokenErrorCode : TokenErrorCode
): RuntimeException(), BaseErrorCode {

    override fun getResponse(): ResponseDTO {
        var map : MutableMap<String, Any> = mutableMapOf()
        map["error-message"] = tokenErrorCode.message
        map["stateCode"] = tokenErrorCode.stateCode
        return ResponseDTO(isSucess = false, httpStatusCode = tokenErrorCode.httpStatusCode, result = map)
    }
}