package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class ChatSelfChatException : BaseException(ErrorCode.CHAT_ROOM_SELF_CHAT)
