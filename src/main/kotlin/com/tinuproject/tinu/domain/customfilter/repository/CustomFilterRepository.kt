package com.tinuproject.tinu.domain.customfilter.repository

import com.tinuproject.tinu.domain.entity.CustomFilter
import org.springframework.data.jpa.repository.JpaRepository

interface CustomFilterRepository : JpaRepository<CustomFilter, Long> {
    fun findCustomFilterById(id : Long) : CustomFilter?
}