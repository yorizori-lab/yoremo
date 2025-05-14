package com.yorizori.yoremo.adapter.`in`.web.recipes

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.domain.recipes.service.GetRecipesService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes/v1")
class RecipesController(
    private val getRecipesService: GetRecipesService,
) {
    @GetMapping("/recipes/{id}")
    fun get(
        @PathVariable id: Int
    ): GetRecipes.Response {
        return getRecipesService.getRecipes(id)
    }
}