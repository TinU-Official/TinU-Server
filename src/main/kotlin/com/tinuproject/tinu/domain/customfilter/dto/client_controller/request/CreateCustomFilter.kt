package com.tinuproject.tinu.domain.customfilter.dto.client_controller.request

data class CreateCustomFilter(
    val filterName : String,
    val category : List<Long>,
    val maxPrice : Int,
    val minPrice : Int,
    val onlySell : Boolean
)
