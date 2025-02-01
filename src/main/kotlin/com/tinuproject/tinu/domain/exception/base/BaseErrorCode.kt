package com.tinuproject.tinu.domain.exception.base

import com.tinuproject.tinu.DTO.ResponseDTO

interface BaseErrorCode {
    fun getResponse(): ResponseDTO?
}