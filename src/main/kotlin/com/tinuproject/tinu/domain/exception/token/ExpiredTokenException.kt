package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseErrorCode
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ResponseDTO
import java.lang.RuntimeException

class ExpiredTokenException(): BaseException(tokenErrorCode = TokenErrorCode.TOKEN_EXPIRED) {
}