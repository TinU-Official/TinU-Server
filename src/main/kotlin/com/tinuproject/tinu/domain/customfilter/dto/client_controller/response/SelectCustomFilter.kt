package com.tinuproject.tinu.domain.customfilter.dto.client_controller.response

import io.swagger.v3.oas.annotations.media.Schema


data class SelectCustomFilter(
    @Schema(description = "필터 ID", example = "1")
    val filterId : Long,
    @Schema(description = "커스텀 필터명", example = "나만의 검색 필터")
    val filterName: String,
    @Schema(description = "카테고리 Id 집합", example = """[1,2,3]""")
    val category: MutableList<Long>,
    @Schema(description = "최대 가격", example = "10000")
    val maxPrice: Int?,
    @Schema(description = "최소 가격", example = "1000")
    val minPrice: Int?,
    @Schema(description = "판매중 여부(true : 판매중인 것만, false : 판매중 + 판매완료)", example = "true")
    val onlySell: Boolean?
)
