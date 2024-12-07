package com.tinuproject.tinu.domain.exception.base

class ErrorResponseDTO(
    val isSucess : Boolean,
    val httpStatusCode : Int,
    val errorCode : String,
    val result : Any
) {

}