package com.yorizori.yoremo.domain.categories.port

import com.yorizori.yoremo.adapter.out.persistence.categories.CategoriesJpaRepository
import com.yorizori.yoremo.domain.categories.entity.Categories
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class CategoriesRepository(
    private val categoriesJpaRepository: CategoriesJpaRepository
) {
    fun findById(id: Long): Categories? {
        return categoriesJpaRepository.findById(id).getOrNull()
    }
}