package com.tinuproject.tinu.infra.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("apple")
class AppleProperties {
    lateinit var teamId : String

    lateinit var loginKey : String

    lateinit var clientId:String

    lateinit var redirectUrl : String

    lateinit var keyPath:String

    lateinit var tokenUrl : String

    lateinit var authUrl : String
}