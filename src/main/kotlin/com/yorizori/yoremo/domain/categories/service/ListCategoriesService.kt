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
    fun listByType(request: ListCategories.Request): ListCategories.Response {
        val categories = categoriesRepository.findAllByCategoryType(request.categoryType)

        return ListCategories.Response(
            categories = categories.map { category ->
                ListCategories.ResponseItem(
                    categoryId = category.categoryId!!,
                    name = category.name,
                    categoryType = category.categoryType,
                    description = category.description
                )
            }
        )
    }
}
