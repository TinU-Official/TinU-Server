package com.tinuproject.tinu.domain.entity

import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long?= null,

    @Column(unique = true)
    var userId : UUID,

    @Column(unique = true)
    var token : String
){
}