package com.tinuproject.tinu.domain.post.repository

import com.tinuproject.tinu.domain.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
    fun findPostById(id: Long): Post?
}