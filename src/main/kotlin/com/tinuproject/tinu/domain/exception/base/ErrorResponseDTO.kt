package com.tinuproject.tinu.domain.exception.base

class ErrorResponseDTO(
    val isSuccess : Boolean,
    val httpStatusCode : Int,
    val errorCode : String,
    val result : Any
) {

}