package com.tinuproject.tinu.domain.exception.mail

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NeedEmailAuthException() : BaseException(ErrorCode.NEED_EMAIL_AUTH) {
}