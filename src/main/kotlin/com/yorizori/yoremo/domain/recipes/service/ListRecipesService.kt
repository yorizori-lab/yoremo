package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesSearchCommand
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListRecipesService(
    private val recipesRepository: RecipesRepository
) {
    @Transactional(readOnly = true)
    fun search(request: SearchRecipes.Request): SearchRecipes.Response {
        val recipes = recipesRepository.search(
            command = RecipesSearchCommand(
                categoryTypeId = request.categoryTypeId,
                categorySituationId = request.categorySituationId,
                categoryIngredientId = request.categoryIngredientId,
                categoryMethodId = request.categoryMethodId,
                difficulty = request.difficulty,
                tags = request.tags
            ),
            pageable = PageRequest.of(request.page!!, request.pageSize!!)
        )

        val content = recipes.content.map {
            SearchRecipes.ResponseItem(
                recipeId = it.recipeId!!,
                title = it.title,
                description = it.description,
                ingredients = it.ingredients,
                seasonings = it.seasonings,
                instructions = it.instructions,
                categoryType = it.categoryType?.name,
                categorySituation = it.categorySituation?.name,
                categoryIngredient = it.categoryIngredient?.name,
                categoryMethod = it.categoryMethod?.name,
                prepTime = it.prepTime,
                cookTime = it.cookTime,
                servingSize = it.servingSize,
                difficulty = it.difficulty?.description,
                imageUrl = it.imageUrl,
                tags = it.tags,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }

        return SearchRecipes.Response(
            totalCount = recipes.totalElements.toInt(),
            recipes = content
        )
    }
}
