package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.domain.foods.port.FoodsRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class GetRecipesService(
    private val recipesRepository: RecipesRepository,
    private val foodsRepository: FoodsRepository
) {

    @Transactional(readOnly = true)
    fun getRecipes(id: Long): GetRecipes.Response {
        val recipes = recipesRepository.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "recipes not found with id: $id"
            )

        val foods = foodsRepository.findByRecipeId(id)

        return GetRecipes.Response(
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
            updatedAt = recipes.updatedAt,
            caloriesPer100g = foods.caloriesPer100g
        )
    }
}
