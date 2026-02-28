package com.tinuproject.tinu.domain.chat.exception

import com.tinuproject.tinu.global.exception.base.BaseException
import com.tinuproject.tinu.global.exception.base.ErrorCode

class ChatRoomNotFoundException : BaseException(ErrorCode.CHAT_ROOM_NOT_FOUND)
