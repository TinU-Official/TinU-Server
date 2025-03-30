package com.tinuproject.tinu.domain.exception.scrap

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class ScrapAlreadyExistException: BaseException(ErrorCode.SCRAP_ALREADY_EXIST) {
}