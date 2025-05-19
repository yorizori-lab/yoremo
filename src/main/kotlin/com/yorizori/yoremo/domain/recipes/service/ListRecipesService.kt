package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.ListRecipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListRecipesService (
    private val recipesRepository: RecipesRepository
) {
    @Transactional(readOnly = true)
    fun listByFilters(request: ListRecipes.Request): ListRecipes.Response {
        val recipes = recipesRepository.findAllByFilters(
            categoryTypeId = request.categoryTypeId,
            categorySituationId = request.categorySituationId,
            categoryIngredientId = request.categoryIngredientId,
            categoryMethodId = request.categoryMethodId,
            difficulty = request.difficulty,
            tags = request.tags
        )

        return ListRecipes.Response(
            recipes = recipes.map { recipes ->
                ListRecipes.ResponseItem(
                    recipeId = recipes.recipeId!!,
                    title = recipes.title,
                    description = recipes.description,
                    ingredients = recipes.ingredients,
                    seasonings = recipes.seasonings,
                    instructions = recipes.instructions,
                    categoryType = recipes.categoryType?.categoryId,
                    categorySituation = recipes.categorySituation?.categoryId,
                    categoryIngredient = recipes.categoryIngredient?.categoryId,
                    categoryMethod = recipes.categoryMethod?.categoryId,
                    prepTime = recipes.prepTime,
                    cookTime = recipes.cookTime,
                    servingSize = recipes.servingSize,
                    difficulty = recipes.difficulty,
                    imageUrl = recipes.imageUrl,
                    tags = recipes.tags,
                    createdAt = recipes.createdAt,
                    updatedAt = recipes.updatedAt
                )
            }
        )
    }
}
