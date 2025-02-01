package com.tinuproject.tinu.security.oauth2.dto

import java.util.*

class UserInfoDto (
    var uuid : UUID,

    var name : String,

    var providerId : String,

    var provider : String
    ) {
}