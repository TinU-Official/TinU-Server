package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class ChatReadInvalidCursorException : BaseException(ErrorCode.CHAT_READ_INVALID_CURSOR)
