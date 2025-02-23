package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.domain.exception.member.ExistEmailException
import com.tinuproject.tinu.domain.exception.mail.NotExistCodeException
import com.tinuproject.tinu.domain.exception.mail.NotMatchCodeException
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.university.repository.UniversityRepository
import com.tinuproject.tinu.web.email.dto.client_controller.EmailAuthRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailCodeCheckRequestDTO
import com.tinuproject.tinu.web.email.repository.EMailRepository
import com.tinuproject.tinu.web.email.entity.EMailAuth
import com.tinuproject.tinu.web.email.util.MailManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RegisterServiceImpl(
    val mailSender: MailManager,
    val eMailRepository: EMailRepository,
    val memberRepository : MemberRepository,
    val universityRepository: UniversityRepository
):RegisterService {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun checkEmailValidation(userId: UUID, email: String): Boolean {
        val domain = email.split("@")[1]

        val university = universityRepository.findByDomain(domain)

        university?: throw NotExistDomainException()

        val member = memberRepository.findMemberByUserId(userId)

        if(member!=null){ throw ExistEmailException()}

        return true
    }


    @Transactional
    override fun sendMail(userId : UUID, emailAuthRequestDTO: EmailAuthRequestDTO) {

        val existEMail = eMailRepository.findByeMail(emailAuthRequestDTO.email)
        if(existEMail!=null){
            log.info((emailAuthRequestDTO.email + " 계정으로 보낸 기존 인증코드를 삭제합니다"))
            eMailRepository.delete(existEMail)
        }
        val code = mailSender.sendMail(emailAuthRequestDTO.email)
        eMailRepository.save(EMailAuth(userId = userId, eMail = emailAuthRequestDTO.email, code = code))
    }


    @Transactional
    override fun checkCode(userId : UUID, emailCodeCheckRequestDTO: EmailCodeCheckRequestDTO) : Boolean {
        val eMailAuth = eMailRepository.findByUserId(userId)
        eMailAuth ?: throw NotExistCodeException()

        return if(eMailAuth.code == emailCodeCheckRequestDTO.code){

            eMailAuth.approve = true

            eMailRepository.save(eMailAuth)

            true

        } else{
            throw NotMatchCodeException()
        }
    }


}