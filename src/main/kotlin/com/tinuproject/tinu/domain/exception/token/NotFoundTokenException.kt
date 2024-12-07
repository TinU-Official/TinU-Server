package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseErrorCode
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ResponseDTO
import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class NotFoundTokenException(
) : BaseException(tokenErrorCode = TokenErrorCode.TOKEN_MISSING) {

}