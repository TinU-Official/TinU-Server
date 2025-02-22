package com.tinuproject.tinu.web.email.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID



/*
    EMailAuth와 Register 객체를 나눌 필요가 느껴짐
        사유 : 이후 레디스를 도입하였을때 인증 코드 유효 시간 설정을 하기 위해
    EMailAuth - userId와 code만 존재
    Register  - userId, Email, nickName 존재
 */
@Entity
class EMailAuth(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column(columnDefinition = "BINARY(16)", unique = true)
    var userId : UUID,

    @Column(name = "eMail")
    var eMail : String,

    @Column(name = "code")
    var code : String,

    @Column(name = "approve")
    var approve : Boolean = false
) {
}