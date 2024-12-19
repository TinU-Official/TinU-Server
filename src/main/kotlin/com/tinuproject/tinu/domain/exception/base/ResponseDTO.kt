package com.tinuproject.tinu.domain.exception.base

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isSuccess","httpStatusCode", "result")
class ResponseDTO(
    @JsonProperty("isSuccess")
    val isSuccess : Boolean?,
    @JsonProperty("httpStatusCode")
    val httpStatusCode : Int?,
    @JsonProperty("result")
    val result : Any?
) {
}