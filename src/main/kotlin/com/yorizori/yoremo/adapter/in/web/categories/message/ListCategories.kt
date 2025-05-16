package com.yorizori.yoremo.adapter.`in`.web.categories.message

import com.yorizori.yoremo.domain.categories.entity.Categories

abstract class ListCategories {
    data class Request(
        val categoryType: Categories.Type
    )

    data class Response(
        val categories: List<ResponseItem>
    )

    data class ResponseItem(
        val categoryId: Long,
        val name: String,
        val categoryType: Categories.Type,
        val description: String?
    )
}
