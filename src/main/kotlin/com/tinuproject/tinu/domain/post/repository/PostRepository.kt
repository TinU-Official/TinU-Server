package com.tinuproject.tinu.domain.post.repository

<<<<<<< HEAD
import com.tinuproject.tinu.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
    fun findPostById(postId: Long): Post?
=======
import com.tinuproject.tinu.domain.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun findPostById(postId: Long) : Post?
>>>>>>> 950b29e ([Feat] : 채팅방 생성 api 완성('/create'))
}