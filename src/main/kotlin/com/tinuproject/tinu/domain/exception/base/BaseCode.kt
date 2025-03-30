package com.tinuproject.tinu.domain.exception.base

import com.tinuproject.tinu.DTO.ResponseDTO

interface BaseCode {
    fun <T> getResponse(): ResponseDTO<T>?
}