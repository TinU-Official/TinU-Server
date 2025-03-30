package com.tinuproject.tinu.domain.exception.s3

import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.domain.exception.base.ErrorCode

class UploadSizeOutOfRangeException() : BaseException(ErrorCode.UPLOAD_SIZE_OUT_OF_RANGE) {
}