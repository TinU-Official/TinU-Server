package com.tinuproject.tinu.infra.security.exception.auth

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class NeedLoginException : BaseException(ErrorCode.NEED_LOGIN) {
}