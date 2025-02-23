package com.tinuproject.tinu.domain.exception.chat

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotAuthorityCreateChatException() : BaseException(ErrorCode.NOT_AUTHORITY_FOR_CREATE_CHAT) {
}