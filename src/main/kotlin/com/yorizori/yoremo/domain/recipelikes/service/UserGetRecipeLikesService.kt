package com.yorizori.yoremo.domain.recipelikes.service

import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.UserGetRecipeLikes
import com.yorizori.yoremo.domain.recipecomments.port.RecipeCommentsRepository
import com.yorizori.yoremo.domain.recipelikes.port.RecipeLikesRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserGetRecipeLikesService(
    private val recipeLikesRepository: RecipeLikesRepository,
    private val recipesRepository: RecipesRepository,
    private val recipeCommentsRepository: RecipeCommentsRepository
) {

    @Transactional
    fun userGetRecipeLikes(
        request: UserGetRecipeLikes.Request,
        userId: Long
    ): UserGetRecipeLikes.Response {
        val likedRecipePage = recipeLikesRepository.findByUserIdOrderByCreatedAtDesc(
            userId = userId,
            pageable = PageRequest.of(
                request.page ?: 0,
                request.pageSize ?: 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )
        )
        val totalCount = recipeLikesRepository.countByUserId(userId)

        val recipeIds = likedRecipePage.content.map { it.recipeId }

        val recipeMap = recipesRepository.findAllById(recipeIds).associateBy { it.recipeId!! }

        val likesCountMap = recipeLikesRepository.countByRecipeIdIn(recipeIds)
        val commentsCountMap = recipeCommentsRepository.countByRecipeIdIn(recipeIds)

        val responseItems = likedRecipePage.content.mapNotNull { recipeLike ->
            val recipe = recipeMap[recipeLike.recipeId] ?: return@mapNotNull null

            UserGetRecipeLikes.ResponseItem(
                recipeId = recipe.recipeId!!,
                title = recipe.title,
                description = recipe.description,
                ingredients = recipe.ingredients,
                seasonings = recipe.seasonings,
                instructions = recipe.instructions,
                categoryType = recipe.categoryType?.name,
                categorySituation = recipe.categorySituation?.name,
                categoryIngredient = recipe.categoryIngredient?.name,
                categoryMethod = recipe.categoryMethod?.name,
                prepTime = recipe.prepTime,
                cookTime = recipe.cookTime,
                servingSize = recipe.servingSize,
                difficulty = recipe.difficulty?.description,
                imageUrl = recipe.imageUrl,
                tags = recipe.tags,
                totalLikes = likesCountMap[recipe.recipeId] ?: 0L,
                totalComments = commentsCountMap[recipe.recipeId] ?: 0L,
                createdAt = recipe.createdAt,
                updatedAt = recipe.updatedAt
            )
        }

        return UserGetRecipeLikes.Response(
            totalCount = totalCount.toInt(),
            recipes = responseItems
        )
    }
}
