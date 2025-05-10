package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class NotCreatedChatException() : BaseException(ErrorCode.NOT_CREATED_CHAT_ID)  {
}