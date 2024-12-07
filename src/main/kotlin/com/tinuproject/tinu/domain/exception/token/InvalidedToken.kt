package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseException

class InvalidedToken:BaseException(TokenErrorCode.TOKEN_INVALIDED) {
}