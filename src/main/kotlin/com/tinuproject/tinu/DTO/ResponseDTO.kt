package com.tinuproject.tinu.DTO

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseDTO<T>(
        @JsonProperty("success") val isSuccess: Boolean,
        @JsonProperty("stateCode") val stateCode: Int,
        @JsonProperty("result") val result: T?
)
