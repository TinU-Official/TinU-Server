package com.tinuproject.tinu.domain.university.repository

import com.tinuproject.tinu.domain.entity.University
import org.springframework.data.repository.CrudRepository

interface UniversityRepository : CrudRepository<University, Long> {
    fun findByDomain(domain : String) : University?
}