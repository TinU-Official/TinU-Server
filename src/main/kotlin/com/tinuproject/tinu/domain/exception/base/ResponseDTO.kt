package com.tinuproject.tinu.domain.exception.base

class ResponseDTO(
    val isSucess : Boolean?,
    val httpStatusCode : Int?,
    val result : Any?
) {


}