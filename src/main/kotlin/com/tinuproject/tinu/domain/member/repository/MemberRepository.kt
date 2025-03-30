package com.tinuproject.tinu.domain.member.repository

import com.tinuproject.tinu.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository : JpaRepository<Member, Long> {
    fun findMemberByEmail(email: String): Member?

    fun findMemberByUserId(userId: UUID) : Member?

    fun findMemberByNickname(nickName : String) : Member?

    fun existsByUserId(userId : UUID) : Boolean

    fun existsByNickname(name : String) : Boolean

    
}