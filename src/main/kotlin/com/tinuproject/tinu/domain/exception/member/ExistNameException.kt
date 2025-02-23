package com.tinuproject.tinu.domain.exception.member

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class ExistNameException() : BaseException(ErrorCode.MEMBER_EXIST_NICKNAME) {
}