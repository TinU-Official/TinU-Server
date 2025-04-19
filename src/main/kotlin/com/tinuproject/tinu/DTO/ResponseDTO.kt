package com.tinuproject.tinu.DTO

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ResponseDTO<T>(
        @JsonProperty("success") val isSuccess: Boolean,
        @JsonProperty("stateCode") @Schema(defaultValue = "200") val stateCode: Int,
        @JsonProperty("result") val result: T?
)
