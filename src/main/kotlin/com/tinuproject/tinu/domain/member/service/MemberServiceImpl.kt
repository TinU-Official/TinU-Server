package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.domain.entity.Member
import com.tinuproject.tinu.domain.enums.Gender
import com.tinuproject.tinu.domain.exception.mail.NeedEmailAuthException
import com.tinuproject.tinu.domain.exception.member.ExistEmailException
import com.tinuproject.tinu.domain.exception.member.ExistNameException
import com.tinuproject.tinu.domain.exception.member.ExistMemberException
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.member.dto.client_controller.RegisterRequestDTO
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.socialmember.repository.SocialMemberRepository
import com.tinuproject.tinu.domain.university.repository.UniversityRepository
import com.tinuproject.tinu.web.email.repository.EMailRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.log

@Service
class MemberServiceImpl(
    val memberRepository: MemberRepository,
    val universityRepository: UniversityRepository,
    val emailAuthRepository: EMailRepository,
    val socialMemberRepository: SocialMemberRepository
):MemberService {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    override fun registerMember(userId: UUID, registerRequestDTO: RegisterRequestDTO) {
        //이미 회원가입이 완료된 유저가 또 회원가입 요청하는 것을 방지.
        if(memberRepository.existsByUserId(userId)){
           throw ExistMemberException()
        }

        val eMailAuth = emailAuthRepository.findByUserId(userId)
        
        //이메일 인증이 진행되지 않은 유저
        if(eMailAuth==null||!eMailAuth.approve||eMailAuth.eMail!=registerRequestDTO.eMail){
                throw NeedEmailAuthException()
        }


        val university = universityRepository.findByDomain(registerRequestDTO.eMail.split("@")[1])

        university ?: throw NotExistDomainException()

        val socialMember = socialMemberRepository.findByUserId(userId)
        val gender = Gender.valueOf(registerRequestDTO.gender)
        val newMember = Member(
            userId = userId,
            university = university,
            nickname = registerRequestDTO.nickName,
            major = registerRequestDTO.major,
            grade = registerRequestDTO.grade,
            gender = gender,
            profileImageURL = registerRequestDTO.profileImageURL,
            introduction = registerRequestDTO.introduction,
            eMail = registerRequestDTO.eMail,
            mark = 0.0,
            social = socialMember!!.provider
        )
        log.info("회원가입이 완료되었습니다. eMailAuth 관련 데이터를 삭제합니다.")
        emailAuthRepository.delete(eMailAuth)

        memberRepository.save(newMember)
    }

    /*
        이후 닉네임 변경에서 쓰일 수도 있음(아마 정보 수정이 일어나면 통괄 수정으로 가지 않을까 싶지만)
        이 때 본인의 닉네임은 본인이 그대로 사용할 수 있어야하기 때문에
        해당 내용이 IF문에 반영이 되어 있음.
     */
    override fun usableMemberByNickName(userId : UUID, name: String) :Boolean{
        val existMember = memberRepository.findMemberByNickname(name)

        if(existMember !=null&&userId!=existMember.userId) throw ExistNameException()

        return true
    }

    override fun usableMemberByEmail(userId: UUID,email: String) :Boolean{
        val existMember= memberRepository.findMemberByeMail(email)

        if(existMember!=null&&existMember.userId!=userId) throw ExistEmailException()

        return true
    }

}