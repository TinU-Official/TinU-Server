package com.tinuproject.tinu.domain.exception.post

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class HashTagNotFoundException: BaseException(ErrorCode.HASHTAG_NOT_FOUND) {
}