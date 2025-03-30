package com.tinuproject.tinu.domain.exception.s3

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NoSuchKeyException(): BaseException(ErrorCode.NO_SUCH_KEY) {
}