package com.yorizori.yoremo.adapter.`in`.web.recipes

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.domain.recipes.service.CreateRecipesService
import com.yorizori.yoremo.domain.recipes.service.GetRecipesService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes/v1")
class RecipesController(
    private val getRecipesService: GetRecipesService,
    private val createRecipesService: CreateRecipesService
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
}