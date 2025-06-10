package com.yorizori.yoremo.adapter.`in`.web.recipes

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.DeleteRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.UpdateRecipes
import com.yorizori.yoremo.domain.recipes.service.CreateRecipesService
import com.yorizori.yoremo.domain.recipes.service.DeleteRecipesService
import com.yorizori.yoremo.domain.recipes.service.GetRecipesService
import com.yorizori.yoremo.domain.recipes.service.ListRecipesService
import com.yorizori.yoremo.domain.recipes.service.UpdateRecipesService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipes/v1")
class RecipesController(
    private val getRecipesService: GetRecipesService,
    private val createRecipesService: CreateRecipesService,
    private val updateRecipesService: UpdateRecipesService,
    private val deleteRecipesService: DeleteRecipesService,
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
        @RequestBody request: CreateRecipes.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): CreateRecipes.Response {
        return createRecipesService.create(request, authentication.userId)
    }

    @PutMapping("/recipes/{id}")
    fun update(
        request: UpdateRecipes.PathVariable,
        @RequestBody updateRequest: UpdateRecipes.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): UpdateRecipes.Response {
        return updateRecipesService.update(request.id, updateRequest, authentication.userId)
    }

    @DeleteMapping("/recipes/{id}")
    fun delete(
        request: DeleteRecipes.PathVariable,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): DeleteRecipes.Response {
        return deleteRecipesService.delete(request.id, authentication.userId)
    }

    @GetMapping("/recipes/search")
    fun search(
        request: SearchRecipes.Request
    ): SearchRecipes.Response {
        return searchRecipesService.search(request)
    }
}
