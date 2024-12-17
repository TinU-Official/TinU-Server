package com.tinuproject.tinu.domain.token.refreshtoken.repository

import com.tinuproject.tinu.domain.entity.RefreshToken
import jakarta.persistence.ManyToOne
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

interface RefreshTokenRepository:CrudRepository<RefreshToken, Long> {

    fun findByUserId(userId : UUID) : RefreshToken

    @Transactional
    @Modifying
    fun deleteByUserId(userId : UUID)
}