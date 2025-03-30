package com.tinuproject.tinu.domain.exception.post

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class PostNotFoundException: BaseException(ErrorCode.POST_NOT_FOUND) {
}