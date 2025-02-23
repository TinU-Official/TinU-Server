package com.tinuproject.tinu.web.email.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class EmailConfig(



) {
    @Value("\${spring.mail.host}")
    val host : String = ""

    @Value("\${spring.mail.port}")
    private val port = 0

    @Value("\${spring.mail.username}")
    private val username: String? = null

    @Value("\${spring.mail.password}")
    private val password: String? = null

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    private val auth = false

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    private val starttlsEnable = false

    @Value("\${spring.mail.properties.mail.smtp.starttls.required}")
    private val starttlsRequired = false

    @Value("\${spring.mail.properties.mail.smtp.connectiontimeout}")
    private val connectionTimeout = 0

    @Value("\${spring.mail.properties.mail.smtp.timeout}")
    private val timeout = 0

    @Value("\${spring.mail.properties.mail.smtp.writetimeout}")
    private val writeTimeout = 0


    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        mailSender.defaultEncoding = "UTF-8"
        mailSender.setJavaMailProperties(getMailProperties())
        return mailSender
    }

    private fun getMailProperties(): Properties {
        val properties = Properties()
        properties.put("mail.smtp.auth", auth)
        properties.put("mail.smtp.starttls.enable", starttlsEnable)
        properties.put("mail.smtp.starttls.required", starttlsRequired)
        properties.put("mail.smtp.connectiontimeout", connectionTimeout)
        properties.put("mail.smtp.timeout", timeout)
        properties.put("mail.smtp.writetimeout", writeTimeout)
        return properties
    }
}