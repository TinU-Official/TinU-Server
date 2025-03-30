package com.tinuproject.tinu.domain.exception.s3

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class InvalidETagException(): BaseException(ErrorCode.INVALID_ETAG) {
}