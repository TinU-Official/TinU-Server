package com.tinuproject.tinu.domain.exception.base

enum class ErrorCode(
    val httpStatusCode : Int,
    val stateCode : String?,
    val message :String
) {
    TOKEN_MISSING(httpStatusCode = 401, stateCode = "TOKEN_MISSING", message = "토큰이 존재하지 않습니다."),
    TOKEN_INVALIDED(httpStatusCode = 401, stateCode = "TOKEN_INVALIDED", message = "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(httpStatusCode = 401, stateCode = "TOKEN_EXPIRED", message = "토큰이 만료되었습니다.");


}