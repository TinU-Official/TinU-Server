package com.tinuproject.tinu.domain.exception.s3

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotAllowedExtensionException(): BaseException(ErrorCode.NOT_ALLOWED_EXTENSION) {
}