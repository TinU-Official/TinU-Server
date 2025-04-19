package com.tinuproject.tinu.domain.exception.member

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NeedRegistException () : BaseException(ErrorCode.MEMBER_NEED_REGIST){
}