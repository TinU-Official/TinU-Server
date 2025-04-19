package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.domain.exception.member.ExistEmailException
import com.tinuproject.tinu.domain.exception.mail.NotExistCodeException
import com.tinuproject.tinu.domain.exception.mail.NotMatchCodeException
import com.tinuproject.tinu.domain.exception.member.ExistMemberException
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.university.repository.UniversityRepository
import com.tinuproject.tinu.web.email.dto.client_controller.EmailAuthRequestDTO
import com.tinuproject.tinu.web.email.dto.client_controller.EmailCodeCheckRequestDTO
import com.tinuproject.tinu.web.email.repository.EmailRepository
import com.tinuproject.tinu.web.email.entity.EmailAuth
import com.tinuproject.tinu.web.email.util.MailManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RegisterServiceImpl(
    val mailSender: MailManager,
    val emailRepository: EmailRepository,
    val memberRepository : MemberRepository,
    val universityRepository: UniversityRepository
):RegisterService {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun checkEmailValidation(userId: UUID, email: String): Boolean {

        //Email의 domain을 추출 (ex kyonggi.ac.kr)
        val domain = email.split("@")[1]

        //해당 도메인이 우리 서비스에서 관리하는 대학 도메인인지 확인
        val university = universityRepository.findByDomain(domain)

        //없는 대학이라면 관리하지 않는 도메인 예외 발생
        university?: throw NotExistDomainException()

        //해당 멤버가 이미 회원가입을 진행했는지 확인하기위해 유저 조회
        var member = memberRepository.findMemberByUserId(userId)

        //조회된 유저가 있다면 이미 회원가입을 완료한 회원임을 알리는 예외 발생
        if(member!=null){ throw ExistMemberException()}

        //TINU-151 추가 로직(이메일 사용 가능 여부 확인 시 해당 이메일로 인증을 받은 사람이 있는지 검증하지 않았었어서 이를 추가.
        member = memberRepository.findMemberByEmail(email)

        //TINU-151 사용 중인 이메일이라면 이미 사용중인 이메일임을 알리는 예외 발생
        if(member!=null){ throw ExistEmailException()}

        return true
    }


    @Transactional
    override fun sendMail(userId : UUID, emailAuthRequestDTO: EmailAuthRequestDTO) {

        val existEMail = emailRepository.findByEmail(emailAuthRequestDTO.email)
        if(existEMail!=null){
            log.info((emailAuthRequestDTO.email + " 계정으로 보낸 기존 인증코드를 삭제합니다"))
            emailRepository.delete(existEMail)
            emailRepository.flush()
        }
        val code = mailSender.sendMail(emailAuthRequestDTO.email)
        emailRepository.save(EmailAuth(userId = userId, email = emailAuthRequestDTO.email, code = code))
    }


    @Transactional
    override fun checkCode(userId : UUID, emailCodeCheckRequestDTO: EmailCodeCheckRequestDTO) : Boolean {
        val emailAuth = emailRepository.findByUserId(userId)
        emailAuth ?: throw NotExistCodeException()

        return if(emailAuth.code == emailCodeCheckRequestDTO.code){

            emailAuth.approve = true

            emailRepository.save(emailAuth)

            true

        } else{
            throw NotMatchCodeException()
        }
    }


}