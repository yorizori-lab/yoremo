package com.yorizori.yoremo.domain.categories.port

import com.yorizori.yoremo.adapter.out.persistence.categories.CategoriesJpaRepository
import com.yorizori.yoremo.domain.categories.entity.Categories
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class CategoriesRepository(
    private val categoriesJpaRepository: CategoriesJpaRepository
) {
    fun findById(id: Long): Categories? {
        return categoriesJpaRepository.findById(id).getOrNull()
    }

    fun findByIdIn(ids: List<Long>): List<Categories> {
        return categoriesJpaRepository.findByCategoryIdIn(ids)
    }

    fun findAllByCategoryType(categoryType: Categories.Type): List<Categories> {
        return categoriesJpaRepository.findAllByCategoryType(categoryType)
    }
}
