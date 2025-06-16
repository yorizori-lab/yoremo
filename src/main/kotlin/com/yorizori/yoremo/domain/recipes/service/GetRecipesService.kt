package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.domain.recipeviewlogs.entity.RecipeViewLogs
import com.yorizori.yoremo.domain.recipeviewlogs.port.RecipeViewLogsRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.net.InetAddress
import java.time.LocalDateTime

@Service
class GetRecipesService(
    private val recipeViewLogsRepository: RecipeViewLogsRepository
) {

    @Transactional
    fun getRecipes(
        id: Long,
        ipAddress: InetAddress,
        userId: Long?,
        userAgent: String?
    ): GetRecipes.Response {
        val result = recipeViewLogsRepository.findRecipeWithRecentViewCheck(id, ipAddress)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "recipes not found with id: $id"
            )

        val (recipes, recentViewLog) = result

        if (recentViewLog == null) {
            recipeViewLogsRepository.save(
                RecipeViewLogs(
                    recipeId = id,
                    userId = userId,
                    ipAddress = ipAddress,
                    userAgent = userAgent,
                    viewedAt = LocalDateTime.now()
                )
            )
        }

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
            caloriesPer100g = recipes.food?.caloriesPer100g,
            viewCount = recipeViewLogsRepository.countLogsByRecipeId(id) + recipes.viewCount
        )
    }
}
