package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseException

class InvalidedTokenException(

):BaseException(tokenErrorCode=TokenErrorCode.TOKEN_INVALIDED) {}