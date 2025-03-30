package com.tinuproject.tinu.domain.exception.customfilter

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class NotExistCustomFilter():BaseException(ErrorCode.FILTER_NOT_EXIST) {
}