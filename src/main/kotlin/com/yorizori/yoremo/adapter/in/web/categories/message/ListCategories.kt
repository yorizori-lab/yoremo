package com.yorizori.yoremo.adapter.`in`.web.categories.message

class ListCategories {
    data class Request(
        val categoryType: String
    )

    data class Response(
        val categories: List<CategoryDto>
    )

    data class CategoryDto(
        val categoryId: Long,
        val name: String,
        val categoryType: String,
        val description: String?
    )
}