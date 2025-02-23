package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

class Register(
    @Column(columnDefinition = "BINARY(16)", unique = true)
    val userId : UUID,

    @Column(name = "eMail")
    var eMail : String?,

    @Column(name = "nickName")
    var nickName : String?
): BaseEntity()
{
}