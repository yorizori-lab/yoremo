package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.UpdateRecipes
import com.yorizori.yoremo.domain.categories.port.CategoriesRepository
import com.yorizori.yoremo.domain.foods.port.FoodsRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UpdateRecipesService(
    private val recipesRepository: RecipesRepository,
    private val categoriesRepository: CategoriesRepository,
    private val foodsRepository: FoodsRepository
) {
    @Transactional
    fun update(id: Long, request: UpdateRecipes.Request): UpdateRecipes.Response {
        val existingRecipe = recipesRepository.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Recipe not found with id: $id"
            )

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
            tags = request.tags
        )

        val savedRecipes = recipesRepository.save(updatedRecipe)

        val existingFoods = foodsRepository.findByRecipeId(id)

        val updatedFoods = existingFoods.copy(
            name = request.title,
            caloriesPer100g = request.caloriesPer100g
        )

        foodsRepository.save(updatedFoods)

        return UpdateRecipes.Response(
            recipeId = savedRecipes.recipeId!!
        )
    }
}
