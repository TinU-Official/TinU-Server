package com.tinuproject.tinu.domain.exception.university

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotExistDomainException() : BaseException(ErrorCode.UNIVERSITY_NOT_EXIST_DOMAIN) {
}