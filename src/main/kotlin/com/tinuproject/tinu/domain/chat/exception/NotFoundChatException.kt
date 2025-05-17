package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class NotFoundChatException() : BaseException(ErrorCode.NOT_FOUND_CHAT) {
}