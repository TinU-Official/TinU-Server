package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class NotAuthorityDeleteChatException() : BaseException(ErrorCode.NOT_AUTHORITY_FOR_DELETE_CHAT) {
}