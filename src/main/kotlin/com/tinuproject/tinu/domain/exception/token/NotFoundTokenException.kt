package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotFoundTokenException(
) : BaseException(errorCode = ErrorCode.TOKEN_MISSING) {

}