package com.tinuproject.tinu.domain.exception.chat

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class AlreadyExistChatException() : BaseException(ErrorCode.ALREADY_EXIST_CHAT) {
}