package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class GetRecipesService(
    private val recipesRepository: RecipesRepository
) {
    private val logger = LoggerFactory.getLogger(GetRecipesService::class.java)

    @Transactional(readOnly = true)
    fun getRecipes(id: Int): GetRecipes.Response {
        // 디버깅을 위해 로깅 추가
        logger.info("Searching for recipe with ID: $id")
        val recipes = recipesRepository.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "recipes not found with id: $id"
            )

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
            tags = recipes.tagsText,
            createdAt = recipes.createdAt,
            updatedAt = recipes.updatedAt
        )
    }
}