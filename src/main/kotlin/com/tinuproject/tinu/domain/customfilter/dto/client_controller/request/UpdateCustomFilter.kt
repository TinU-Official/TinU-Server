package com.tinuproject.tinu.domain.customfilter.dto.client_controller.request

import org.jetbrains.annotations.NotNull

data class UpdateCustomFilter(
    var filterId : Long?,
    val filterName : String,
    val category : List<Long>,
    val maxPrice : Int?,
    val minPrice : Int?,
    val onlySell : Boolean?
)
