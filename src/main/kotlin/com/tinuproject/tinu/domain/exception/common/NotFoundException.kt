package com.tinuproject.tinu.domain.exception.common

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotFoundException() : BaseException(ErrorCode.NOT_FOUND) {
}