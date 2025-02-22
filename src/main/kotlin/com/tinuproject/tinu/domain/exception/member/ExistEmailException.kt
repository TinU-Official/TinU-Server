package com.tinuproject.tinu.domain.exception.member

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class ExistEmailException() : BaseException(ErrorCode.MEMBER_EXIST_EMAIL) {
}