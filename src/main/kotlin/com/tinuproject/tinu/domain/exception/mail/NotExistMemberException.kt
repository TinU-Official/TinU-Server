package com.tinuproject.tinu.domain.exception.mail

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotExistMemberException():BaseException(ErrorCode.MEMBER_NOT_EXIST) {
}