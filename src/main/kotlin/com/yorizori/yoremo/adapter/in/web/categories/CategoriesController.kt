package com.yorizori.yoremo.adapter.`in`.web.categories

import com.yorizori.yoremo.adapter.`in`.web.categories.message.ListCategories
import com.yorizori.yoremo.domain.categories.service.ListCategoriesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/categories/v1")
class CategoriesController(
    private val listCategoriesService: ListCategoriesService
) {
    @GetMapping("/categories")
    fun listCategories(
        request: ListCategories.Request
    ): ListCategories.Response {
        return listCategoriesService.listByType(request)
    }
}
