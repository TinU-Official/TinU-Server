package com.tinuproject.tinu.domain.exception.token

import com.tinuproject.tinu.domain.exception.base.BaseException

class InvalidedToken(

):BaseException(tokenErrorCode=TokenErrorCode.TOKEN_INVALIDED) {}