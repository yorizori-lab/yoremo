package com.yorizori.yoremo.adapter.`in`.web.recipes

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.domain.recipes.service.CreateRecipesService
import com.yorizori.yoremo.domain.recipes.service.GetRecipesService
import com.yorizori.yoremo.domain.recipes.service.ListRecipesService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipes/v1")
class RecipesController(
    private val getRecipesService: GetRecipesService,
    private val createRecipesService: CreateRecipesService,
    private val searchRecipesService: ListRecipesService
) {
    @GetMapping("/recipes/{id}")
    fun get(
        request: GetRecipes.PathVariable
    ): GetRecipes.Response {
        return getRecipesService.getRecipes(request.id)
    }

    @PostMapping("/recipes")
    fun create(
        @RequestBody request: CreateRecipes.Request
    ): CreateRecipes.Response {
        return createRecipesService.create(request)
    }

    @GetMapping("/recipes/search")
    fun search(
        request: SearchRecipes.Request,
        @PageableDefault(size = 10) pageable: Pageable
    ): SearchRecipes.PageResponse {
        return searchRecipesService.search(request, pageable)
    }
}
