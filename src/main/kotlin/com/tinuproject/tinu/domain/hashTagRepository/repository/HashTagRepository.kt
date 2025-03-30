package com.tinuproject.tinu.domain.hashTagRepository.repository

import com.tinuproject.tinu.domain.entity.HashTag
import org.springframework.data.repository.CrudRepository

interface HashTagRepository: CrudRepository<HashTag, Long> {
    fun findAllByTagNameIn(tagNames : List<String>) : List<HashTag>
}