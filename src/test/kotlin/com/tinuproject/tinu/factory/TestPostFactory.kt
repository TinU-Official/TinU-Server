package com.tinuproject.tinu.factory

import com.tinuproject.tinu.domain.member.entity.Member
import com.tinuproject.tinu.domain.post.entity.Category
import com.tinuproject.tinu.domain.post.entity.Post
import com.tinuproject.tinu.domain.post.repository.PostRepository
import com.tinuproject.tinu.domain.university.entity.University

object TestPostFactory {
    fun create( postRepository: PostRepository, author: Member, buyer : Member?, university : University, category: Category) : Post {
        return postRepository.save(
            Post(
                university = university,
                title = "test",
                body = "test",
                author = author,
                buyer = buyer,
                category = category,
                price = 100,
                isSoldOut = false,
                thumbnail = null
            )
        )
    }
}