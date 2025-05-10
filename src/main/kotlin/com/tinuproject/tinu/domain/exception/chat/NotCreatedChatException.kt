package com.tinuproject.tinu.domain.exception.chat

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotCreatedChatException() : BaseException(ErrorCode.NOT_CREATED_CHAT_ID)  {
}