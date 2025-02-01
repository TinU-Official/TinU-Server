package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class ExpiredTokenException(): BaseException(errorCode = ErrorCode.TOKEN_EXPIRED) {
}