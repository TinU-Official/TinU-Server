package com.tinuproject.tinu.domain.customfilter.service

import com.tinuproject.tinu.domain.category.repository.CategoryRepository
import com.tinuproject.tinu.domain.customcategory.repository.CustomCategoryRepository
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.CreateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.DeleteCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.response.SelectCustomFilter
import com.tinuproject.tinu.domain.customfilter.repository.CustomFilterRepository
import com.tinuproject.tinu.domain.entity.CustomCategory
import com.tinuproject.tinu.domain.entity.CustomFilter
import com.tinuproject.tinu.domain.exception.common.UnauthorizedAccessException
import com.tinuproject.tinu.domain.exception.customfilter.NotExistCustomFilter
import com.tinuproject.tinu.domain.exception.mail.NotExistMemberException
import com.tinuproject.tinu.domain.member.repository.MemberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomFilterServiceImpl(
    val memberRepository: MemberRepository,
    val customFilterRepository: CustomFilterRepository,
    val customCategoryRepository: CustomCategoryRepository,
    val categoryRepository: CategoryRepository
):CustomFilterService {
    val log :Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun getCustomFilter(userId: UUID): List<SelectCustomFilter> {
        val result = mutableListOf<SelectCustomFilter>()

        val member = memberRepository.findMemberByUserId(userId = userId)?:throw NotExistMemberException()

        val customFilters = member.customFilter

        for(customFilter in customFilters){
            val categories = mutableListOf<Long>()

            for(category in customFilter.customCategory){
                categories.add(category.category.id!!)
            }

            result.add(SelectCustomFilter(
                filterId = customFilter.id!!,
                filterName =  customFilter.filterName,
                maxPrice = customFilter.maxPrice,
                minPrice = customFilter.minPrice,
                onlySell = customFilter.onlySell,
                category = categories
            ))
        }

        return result
    }

    @Transactional
    override fun createCustomFilter(userId: UUID, createCustomFilter: CreateCustomFilter) {
        val member = memberRepository.findMemberByUserId(userId)?:throw NotExistMemberException()

        val customFilter = CustomFilter(
            filterName = createCustomFilter.filterName,
            onlySell = createCustomFilter.onlySell,
            maxPrice = createCustomFilter.maxPrice,
            minPrice = createCustomFilter.minPrice,
            member = member
        )

        customFilterRepository.save(customFilter)
        mappingCustomCategory(customFilter, createCustomFilter.category.toMutableList() )
    }

    @Transactional
    override fun updateCustomFilter(userId: UUID, updateCustomFilter: UpdateCustomFilter) {
        val customFilter = customFilterRepository.findCustomFilterById(updateCustomFilter.filterId!!) ?: throw NotExistCustomFilter()

        if(customFilter.member.userId!=userId){
            throw UnauthorizedAccessException()
        }

        customFilter.updateCustomFilter(updateCustomFilter)
        log.info("delete 실행.")
        customCategoryRepository.deleteAllByCustomFilterId(customFilterId = customFilter.id!!)
        mappingCustomCategory(customFilter,updateCustomFilter.category.toMutableList())
    }

    @Transactional
    override fun deleteCustomFilter(userId :UUID, deleteCustomFilter : DeleteCustomFilter){
        val existCustomFilter = customFilterRepository.findCustomFilterById(deleteCustomFilter.filterId)?: throw NotExistCustomFilter()


        if(existCustomFilter.member.userId != userId) throw UnauthorizedAccessException()
        log.info("커스텀 필터 삭제")
        customCategoryRepository.deleteAllByCustomFilterId(existCustomFilter.id!!)
        customFilterRepository.deleteById(deleteCustomFilter.filterId)
        customFilterRepository.flush()
        log.info("커스텀 필터 삭제 완료")
    
    }


    private fun mappingCustomCategory(customFilter: CustomFilter, categorylist : MutableList<Long>){
        val categories = categoryRepository.findAllById(categorylist)

        val customCategories = categories.map { category ->
            CustomCategory(category = category, customFilter = customFilter)
        }

        customCategoryRepository.saveAll(customCategories)
    }

    private fun garbageMappingCustomCategory(customFilter: CustomFilter, categorylist: MutableList<Long>){
        for(i  in categorylist){
            val category = categoryRepository.findById(i).get()

            val customCategory = CustomCategory(
                category = category,
                customFilter = customFilter
            )

            customCategoryRepository.save(customCategory)
        }
    }
}