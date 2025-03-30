package com.tinuproject.tinu.web.email.repository

import com.tinuproject.tinu.web.email.entity.EmailAuth
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailRepository : JpaRepository<EmailAuth, Long> {
    fun findByUserId(userId : UUID) : EmailAuth?

    fun findByEmail(email: String) : EmailAuth?
}