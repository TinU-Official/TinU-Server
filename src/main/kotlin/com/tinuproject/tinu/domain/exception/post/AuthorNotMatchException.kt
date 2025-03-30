package com.tinuproject.tinu.domain.exception.post

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class AuthorNotMatchException: BaseException(ErrorCode.AUTHOR_NOT_MATCH) {
}