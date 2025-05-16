package com.yorizori.yoremo.adapter.`in`.web.categories

import com.yorizori.yoremo.adapter.`in`.web.categories.message.ListCategories
import com.yorizori.yoremo.domain.categories.service.ListCategoriesService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/categories")
class CategoriesController(
    private val listCategoriesService: ListCategoriesService
) {
    @GetMapping
    fun listCategories(@RequestParam categoryType: String): ResponseEntity<ListCategories.Response> {
        val response = listCategoriesService.listByType(categoryType)
        return ResponseEntity.ok(response)
    }
}