package com.tinuproject.tinu.domain.post.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.StringExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.tinuproject.tinu.domain.entity.Post
import com.tinuproject.tinu.domain.entity.QPost
import com.tinuproject.tinu.domain.entity.University
import org.springframework.stereotype.Repository

@Repository
class PostQueryRepositoryImpl(
        private val queryFactory: JPAQueryFactory
): PostQueryRepository {
    val post: QPost = QPost.post

    override fun findPosts(
            university: University,
            cursorId: String?,
            size: Long,
            keyword: String?,
            category: List<Long>?,
            minPrice: Int?,
            maxPrice: Int?,
            onlySell: Boolean
    ): List<Post> {
        return queryFactory
                .selectFrom(post)
                .where(
                        post.university.eq(university),
                        post.isHide.eq(false),
                        customCursor(cursorId),
                        containsTitle(keyword),
                        containsBody(keyword),
                        inCategory(category),
                        betweenPrice(maxPrice, minPrice),
                        eqOnlySell(onlySell)
                )
                .limit(size + 1)
                .orderBy(post.createdAt.desc())
                .fetch()
    }

    override fun customCursor(cursorId: String?): BooleanExpression? {
        if (cursorId.isNullOrBlank()) {
            return null
        }
        return post.id.lt(cursorId.toLong())
    }

    override fun containsTitle(keyword: String?): BooleanExpression? {
        return when {
            keyword.isNullOrBlank() -> post.title.contains(keyword)
            else -> null
        }
    }

    override fun containsBody(keyword: String?): BooleanExpression? {
        return when {
            keyword.isNullOrBlank() -> post.body.contains(keyword)
            else -> null
        }
    }

    override fun inCategory(category: List<Long>?): BooleanExpression? {
        return when {
            category.isNullOrEmpty() -> null
            else -> post.category.id.`in`(category)
        }
    }

    override fun betweenPrice(maxPrice: Int?, minPrice: Int?): BooleanExpression? {
        return when {
            maxPrice != null && minPrice != null && maxPrice >= minPrice -> post.price.between(minPrice, maxPrice)
            minPrice != null -> post.price.goe(minPrice)
            maxPrice != null -> post.price.loe(maxPrice)
            else -> null
        }
    }

    override fun eqOnlySell(onlySell: Boolean): BooleanExpression? {
        return when (onlySell) {
            true -> post.isSell.eq(true)
            false -> null
        }
    }

}