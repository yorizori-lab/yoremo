package com.yorizori.yoremo.domain.categories.service

import com.yorizori.yoremo.adapter.`in`.web.categories.message.ListCategories
import com.yorizori.yoremo.domain.categories.port.CategoriesRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListCategoriesService(
    private val categoriesRepository: CategoriesRepository
) {
    @Transactional(readOnly = true)
    fun listByType(categoryType: String): ListCategories.Response {
        val categories = categoriesRepository.findAllByCategoryType(categoryType)

        val categoryDtos = categories.map { category ->
            ListCategories.CategoryDto(
                categoryId = category.categoryId ?: throw IllegalStateException("카테고리 ID가 null입니다"),
                name = category.name ?: "",
                categoryType = category.categoryType ?: "",
                description = category.description
            )
        }

        return ListCategories.Response(categories = categoryDtos)
    }
}