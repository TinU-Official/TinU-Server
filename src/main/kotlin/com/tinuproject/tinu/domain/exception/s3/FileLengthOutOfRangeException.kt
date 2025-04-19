package com.tinuproject.tinu.domain.exception.s3

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class FileLengthOutOfRangeException(): BaseException(ErrorCode.FILE_LENGTH_OUT_OF_RANGE) {
}