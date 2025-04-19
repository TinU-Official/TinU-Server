package com.tinuproject.tinu.domain.customfilter.dto.client_controller.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema()
data class CreateCustomFilter(
    @Schema(description = "필터 이름", example = "나만의 가전제품")
    val filterName : String,
    @Schema(description = "카테고리 Id 집합", example = """[1,2,3]""")
    val category : List<Long>,
    @Schema(description = "최대 가격", example = "10000")
    val maxPrice : Int?,
    @Schema(description = "최소 가격", example = "1000")
    val minPrice : Int?,
    @Schema(description = "현재 판매 중인지 여부(true면 판매중인 상품 false 면 판매중 + 판매완료)", example = "true")
    val onlySell : Boolean
)
