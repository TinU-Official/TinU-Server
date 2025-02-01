package com.tinuproject.tinu.domain.socialmember.repository

import com.tinuproject.tinu.domain.entity.SocialMember
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SocialMemberRepository:CrudRepository<SocialMember, Long> {
    fun findByUserId(userId : UUID) : SocialMember?

    fun findByProviderId(providerId : String) : SocialMember?
}