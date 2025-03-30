package com.tinuproject.tinu.domain.post.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.tinuproject.tinu.domain.entity.Post
import com.tinuproject.tinu.domain.entity.University

interface PostQueryRepository {
    fun findPosts(
            university: University,
            cursorId: String?,
            size: Long,
            keyword: String?,
            category: List<Long>?,
            minPrice: Int?,
            maxPrice: Int?,
            onlySell: Boolean
    ): List<Post>

    fun customCursor(cursorId: String?): BooleanExpression?

    fun containsTitle(keyword: String?): BooleanExpression?

    fun containsBody(keyword: String?): BooleanExpression?

    fun inCategory(category: List<Long>?): BooleanExpression?

    fun betweenPrice(maxPrice: Int?, minPrice: Int?): BooleanExpression?

    fun eqOnlySell(onlySell: Boolean): BooleanExpression?
}