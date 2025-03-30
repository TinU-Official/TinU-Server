package com.tinuproject.tinu.domain.customfilter.service

import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.CreateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.DeleteCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.customfilter.dto.client_controller.response.SelectCustomFilter
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface CustomFilterService {

    fun getCustomFilter(userId : UUID) : List<SelectCustomFilter>

    fun createCustomFilter(userId : UUID, createCustomFilter: CreateCustomFilter)

    fun updateCustomFilter(userId : UUID, updateCustomFilter: UpdateCustomFilter)

    fun deleteCustomFilter(userId : UUID, deleteCustomFilter: DeleteCustomFilter)
}