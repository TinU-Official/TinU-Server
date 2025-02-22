package com.tinuproject.tinu.domain.exception.mail

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotExistCodeException() : BaseException(ErrorCode.NOT_EXIST_CODE) {
}