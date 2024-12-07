package com.tinuproject.tinu.domain.exception.base

interface BaseErrorCode {
    fun getResponse(): ResponseDTO?
}