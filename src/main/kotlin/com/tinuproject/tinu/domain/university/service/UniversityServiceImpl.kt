package com.tinuproject.tinu.domain.university.service

import com.tinuproject.tinu.domain.entity.University
import com.tinuproject.tinu.domain.exception.university.NotExistDomainException
import com.tinuproject.tinu.domain.university.repository.UniversityRepository
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UniversityServiceImpl(
    val universityRepository: UniversityRepository
) : UniversityService {
    var log : Logger = LoggerFactory.getLogger(this::class.java)
    @Transactional
    override fun testUniversityAdd() {
        universityRepository.deleteAll()

        val existUniversity = universityRepository.findByDomain("kyonggi.ac.kr")

        existUniversity?: run {
            log.info("경기대를 추가합니다.")
            val kyonggi = University(name = "경기대학교", domain = "kyonggi.ac.kr")
            val KGU = University(name = "경기대학교", domain = "kgu.ac.kr")

            universityRepository.save(kyonggi)
            universityRepository.save(KGU)
        }
    }

    override fun existDomain(domain: String) : Boolean{
        val university : University? = universityRepository.findByDomain(domain)

        university?:throw NotExistDomainException()
        return true
    }
}