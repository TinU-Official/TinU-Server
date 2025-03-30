package com.tinuproject.tinu.domain.customcategory.repository

import com.tinuproject.tinu.domain.entity.CustomCategory
import com.tinuproject.tinu.domain.entity.CustomFilter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CustomCategoryRepository : JpaRepository<CustomCategory,Long>  {

    @Query("delete from CustomCategory c where c.customFilter.id = :customFilterId")
    @Modifying
    fun deleteAllByCustomFilterId(@Param("customFilterId") customFilterId: Long)


    fun deleteAllByCustomFilter(customFilter: CustomFilter)


}