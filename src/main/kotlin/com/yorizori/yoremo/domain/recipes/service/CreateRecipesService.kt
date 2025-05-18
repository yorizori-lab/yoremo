package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.domain.categories.port.CategoriesRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class CreateRecipesService(
    private val recipesRepository: RecipesRepository,
    private val categoriesRepository: CategoriesRepository
) {
    @Transactional
    fun create(request: CreateRecipes.Request): CreateRecipes.Response {
        val categoryType = request.categoryTypeId
            ?.let { categoriesRepository.findById(it) }
        val categorySituation = request.categorySituationId
            ?.let { categoriesRepository.findById(it) }
        val categoryIngredient = request.categoryIngredientId
            ?.let { categoriesRepository.findById(it) }
        val categoryMethod = request.categoryMethodId
            ?.let { categoriesRepository.findById(it) }

        if (
            categoryType == null ||
            categoryMethod == null ||
            categorySituation == null ||
            categoryIngredient == null
        ) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "invalid parameter. request: $request"
            )
        }

        val recipes = Recipes(
            title = request.title,
            description = request.description,
            ingredients = request.ingredients,
            seasonings = request.seasonings,
            instructions = request.instructions,
            categoryType = categoryType,
            categorySituation = categorySituation,
            categoryIngredient = categoryIngredient,
            categoryMethod = categoryMethod,
            prepTime = request.prepTime,
            cookTime = request.cookTime,
            servingSize = request.servingSize,
            difficulty = request.difficulty,
            imageUrl = request.imageUrl,
            tags = request.tags // 직접 List<String>으로 전달
        )

        val savedRecipes = recipesRepository.save(recipes)

        return CreateRecipes.Response(
            recipeId = savedRecipes.recipeId!!
        )
    }
}
