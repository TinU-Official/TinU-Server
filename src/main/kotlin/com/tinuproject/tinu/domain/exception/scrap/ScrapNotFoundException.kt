package com.tinuproject.tinu.domain.exception.scrap

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class ScrapNotFoundException: BaseException(ErrorCode.SCRAP_NOT_FOUND) {
}