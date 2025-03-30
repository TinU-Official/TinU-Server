package com.tinuproject.tinu.domain.member.service

import com.tinuproject.tinu.domain.entity.Member
import com.tinuproject.tinu.domain.exception.mail.NeedEmailAuthException
import com.tinuproject.tinu.domain.exception.mail.NotExistMemberException
import com.tinuproject.tinu.domain.exception.member.ExistEmailException
import com.tinuproject.tinu.domain.exception.member.ExistNameException
import com.tinuproject.tinu.domain.exception.member.ExistMemberException
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.member.dto.client_controller.request.RegisterRequestDTO
import com.tinuproject.tinu.domain.member.dto.client_controller.request.UpdateUserInfoRequestDTO
import com.tinuproject.tinu.domain.member.dto.client_controller.response.MemberSearchResponseDTO
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import com.tinuproject.tinu.domain.socialmember.repository.SocialMemberRepository
import com.tinuproject.tinu.domain.university.repository.UniversityRepository
import com.tinuproject.tinu.web.email.entity.EmailAuth
import com.tinuproject.tinu.web.email.repository.EmailRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberServiceImpl(
    val memberRepository: MemberRepository,
    val universityRepository: UniversityRepository,
    val emailAuthRepository: EmailRepository,
    val socialMemberRepository: SocialMemberRepository
):MemberService {
    var log : Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun registerMember(userId: UUID, registerRequestDTO: RegisterRequestDTO) {
        //닉네임 사용 가능 여부 체크
        usableMemberByNickname(userId, registerRequestDTO.nickName)

        //이미 회원가입이 완료된 유저가 또 회원가입 요청하는 것을 방지.
        existMemberByUserId(userId)

        //이메일 인증 체크
        val emailAuth = emailAuthCheck(userId, registerRequestDTO.email)
        
        val university = universityRepository.findByDomain(registerRequestDTO.email.split("@")[1])

        university ?: throw NotExistDomainException()

        val socialMember = socialMemberRepository.findByUserId(userId)
        val newMember = Member(
            userId = userId,
            university = university,
            nickname = registerRequestDTO.nickName,
            major = registerRequestDTO.major,
            grade = registerRequestDTO.grade,
            profileImageURL = registerRequestDTO.profileImageURL,
            introduction = registerRequestDTO.introduction,
            email = registerRequestDTO.email,
            mark = 0.0,
            social = socialMember!!.provider
        )
        log.info("회원가입이 완료되었습니다. eMailAuth 관련 데이터를 삭제합니다.")
        emailAuthRepository.delete(emailAuth)

        memberRepository.save(newMember)
    }

    /*
        이후 닉네임 변경에서 쓰일 수도 있음(아마 정보 수정이 일어나면 통괄 수정으로 가지 않을까 싶지만)
        이 때 본인의 닉네임은 본인이 그대로 사용할 수 있어야하기 때문에
        해당 내용이 IF문에 반영이 되어 있음.
     */
    @Transactional(readOnly = true)
    override fun usableMemberByNickname(userId : UUID, nickName: String) :Boolean{
        val existMember = memberRepository.findMemberByNickname(nickName)

        if(existMember !=null&&userId!=existMember.userId) throw ExistNameException()

        return true
    }


    @Transactional(readOnly = true)
    override fun usableMemberByEmail(userId: UUID,email: String) :Boolean{
        val existMember= memberRepository.findMemberByEmail(email)

        if(existMember!=null&&existMember.userId!=userId){
            throw ExistEmailException()
        }

        return true
    }

    @Transactional(readOnly = true)
    override fun findMemberByUserId(userId: UUID): MemberSearchResponseDTO {
        val member = memberRepository.findMemberByUserId(userId)

        member?:NotExistMemberException()

        return MemberSearchResponseDTO(member!!)

    }

    @Transactional
    override fun updateMember(userId: UUID, updateUserInfoRequestDTO: UpdateUserInfoRequestDTO) {
        val member = memberRepository.findMemberByUserId(userId)

        member?: throw NotExistMemberException()

        usableMemberByNickname(userId = userId, nickName = updateUserInfoRequestDTO.nickname)

        member.updateMemberInfo(updateUserInfoRequestDTO)

        memberRepository.save(member)

    }

    private fun emailAuthCheck(userId :UUID, email : String) : EmailAuth{
        val emailAuth = emailAuthRepository.findByUserId(userId)

        //이메일 인증이 진행되지 않은 유저
        if(emailAuth==null||!emailAuth.approve||emailAuth.email!=email){
            throw NeedEmailAuthException()
        }

        return emailAuth
    }

    private fun existMemberByUserId(userId: UUID){
        if(memberRepository.findMemberByUserId(userId)!=null){
            throw ExistMemberException()
        }
    }

}