package com.tinuproject.tinu.domain.category.repository

import com.tinuproject.tinu.domain.entity.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long>{
    fun findCategoryById(categoryId : Long) : Category?
}