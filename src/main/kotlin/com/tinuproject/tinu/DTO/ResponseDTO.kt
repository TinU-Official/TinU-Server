package com.tinuproject.tinu.DTO

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseDTO(
        @JsonProperty("success") val isSuccess: Boolean,
        @JsonProperty("state") val stateCode: Int,
        @JsonProperty("result") val result: Any
)
