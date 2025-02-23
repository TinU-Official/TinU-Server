package com.tinuproject.tinu.domain.exception.member

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class ExistMemberException() : BaseException(ErrorCode.MEMBER_EXIST) {
}