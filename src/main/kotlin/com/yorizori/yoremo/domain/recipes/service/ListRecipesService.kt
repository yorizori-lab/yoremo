package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesSearchCommand
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListRecipesService(
    private val recipesRepository: RecipesRepository
) {
    @Transactional(readOnly = true)
    fun search(request: SearchRecipes.Request, pageable: Pageable): SearchRecipes.PageResponse {
        val recipes = recipesRepository.search(
            RecipesSearchCommand(
                categoryTypeId = request.categoryTypeId,
                categorySituationId = request.categorySituationId,
                categoryIngredientId = request.categoryIngredientId,
                categoryMethodId = request.categoryMethodId,
                difficulty = request.difficulty,
                tags = request.tags
            ),
            pageable
        )

        // 페이지 결과 변환
        val content = recipes.content.map { recipes ->
            SearchRecipes.ResponseItem(
                recipeId = recipes.recipeId!!,
                title = recipes.title,
                description = recipes.description,
                ingredients = recipes.ingredients,
                seasonings = recipes.seasonings,
                instructions = recipes.instructions,
                categoryType = recipes.categoryType?.name,
                categorySituation = recipes.categorySituation?.name,
                categoryIngredient = recipes.categoryIngredient?.name,
                categoryMethod = recipes.categoryMethod?.name,
                prepTime = recipes.prepTime,
                cookTime = recipes.cookTime,
                servingSize = recipes.servingSize,
                difficulty = recipes.difficulty?.description,
                imageUrl = recipes.imageUrl,
                tags = recipes.tags,
                createdAt = recipes.createdAt,
                updatedAt = recipes.updatedAt
            )
        }

        return SearchRecipes.PageResponse(
            recipes = content,
            totalElements = recipes.totalElements,
            totalPages = recipes.totalPages,
            number = recipes.number,
            size = recipes.size,
            first = recipes.isFirst,
            last = recipes.isLast,
            empty = recipes.isEmpty
        )
    }
}
