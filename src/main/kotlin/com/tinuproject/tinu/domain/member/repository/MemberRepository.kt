package com.tinuproject.tinu.domain.member.repository

import com.tinuproject.tinu.domain.entity.Member
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface MemberRepository : CrudRepository<Member, Long> {
    fun findMemberByeMail(eMail: String): Member?

    fun findMemberByUserId(userId: UUID) : Member?

    fun findMemberByNickname(nickName : String) : Member?

    fun existsByUserId(userId : UUID) : Boolean

    fun existsByNickname(name : String) : Boolean

    
}