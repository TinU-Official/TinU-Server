package com.tinuproject.tinu.domain.customfilter.dto.client_controller.response

data class SelectCustomFilter(
    val filterId : Long,
    val filterName: String,
    val category: MutableList<Long>,
    val maxPrice: Int?,
    val minPrice: Int?,
    val onlySell: Boolean?
)
