package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class AlreadyExistChatException() : BaseException(ErrorCode.ALREADY_EXIST_CHAT) {
}