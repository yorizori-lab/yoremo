package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.UpdateRecipes
import com.yorizori.yoremo.domain.categories.port.CategoriesRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UpdateRecipesService(
    private val recipesRepository: RecipesRepository,
    private val categoriesRepository: CategoriesRepository
) {
    @Transactional
    fun update(id: Long, request: UpdateRecipes.Request, userId: Long): UpdateRecipes.Response {
        val existingRecipe = recipesRepository.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Recipe not found with id: $id"
            )

        if (existingRecipe.userId != userId) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "접근 권한이 없습니다."
            )
        }


        val categories = categoriesRepository.findByIdIn(
            listOfNotNull(
                request.categoryTypeId,
                request.categorySituationId,
                request.categoryIngredientId,
                request.categoryMethodId
            )
        )

        val categoriesMap = categories.associateBy { it.categoryId }

        val updatedRecipe = existingRecipe.copy(
            recipeId = id,
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            seasonings = request.seasonings,
            instructions = request.instructions,
            categoryType = categoriesMap[request.categoryTypeId],
            categorySituation = categoriesMap[request.categorySituationId],
            categoryIngredient = categoriesMap[request.categoryIngredientId],
            categoryMethod = categoriesMap[request.categoryMethodId],
            prepTime = request.prepTime,
            cookTime = request.cookTime,
            servingSize = request.servingSize,
            difficulty = request.difficulty,
            imageUrl = request.imageUrl,
            tags = request.tags,
            food = existingRecipe.food.apply {
                this?.name = request.title
                this?.caloriesPer100g = request.caloriesPer100g
            }
        )

        val savedRecipes = recipesRepository.save(updatedRecipe)

        return UpdateRecipes.Response(
            recipeId = savedRecipes.recipeId!!
        )
    }
}
