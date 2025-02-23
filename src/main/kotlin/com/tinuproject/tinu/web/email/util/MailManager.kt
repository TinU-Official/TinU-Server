package com.tinuproject.tinu.web.email.util

import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.web.email.repository.EMailRepository
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.MessagingException
import org.springframework.stereotype.Component


@Component
class MailManager(
    private val javaMailSender: JavaMailSender,

    @Value("\${spring.mail.username}")
    private val account : String
) : MailSender{
    lateinit var code : String

    //이메일 전송과 관련한 부분.
    override fun createNumber() {
        code = ((Math.random() * 90000).toInt() + 100000).toString() //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    override fun createMail(mail: String?): MimeMessage {
        createNumber()
        val message = javaMailSender.createMimeMessage()
        try {
            message.setFrom(account)
            message.setRecipients(MimeMessage.RecipientType.TO, mail)
            message.subject = "이메일 인증"
            var body = ""
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>"
            body += "<h1>$code</h1>"
            body += "<h3>" + "감사합니다." + "</h3>"
            message.setText(body, "UTF-8", "html")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return message
    }

    override fun sendMail(mail: String?): String {
        val message: MimeMessage = createMail(mail)
        javaMailSender.send(message)
        return code
    }
}