package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class NotAuthorityCreateChatException() : BaseException(ErrorCode.NOT_AUTHORITY_FOR_CREATE_CHAT) {
}