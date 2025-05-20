package com.yorizori.yoremo.adapter.`in`.web.recipes.message

import com.yorizori.yoremo.domain.recipes.entity.Recipes
import java.time.Instant

class SearchRecipes {
    data class Request(
        val categoryTypeId: Long? = null,
        val categorySituationId: Long? = null,
        val categoryIngredientId: Long? = null,
        val categoryMethodId: Long? = null,
        val difficulty: Recipes.Difficulty? = null,
        val tags: List<String>? = null
    )

    data class Response(
        val recipes: List<ResponseItem>
    )

    // 페이징 응답을 위한 새 클래스
    data class PageResponse(
        val recipes: List<ResponseItem>,
        val totalElements: Long,
        val totalPages: Int,
        val number: Int,
        val size: Int,
        val first: Boolean,
        val last: Boolean,
        val empty: Boolean
    )

    data class ResponseItem(
        val recipeId: Long,
        val title: String,
        val description: String?,
        val ingredients: List<Recipes.Ingredient>,
        val seasonings: List<Recipes.Seasoning>,
        val instructions: List<Recipes.Instruction>,
        val categoryType: String?,
        val categorySituation: String?,
        val categoryIngredient: String?,
        val categoryMethod: String?,
        val prepTime: Int?,
        val cookTime: Int?,
        val servingSize: Int?,
        val difficulty: String?,
        val imageUrl: String?,
        val tags: List<String>?,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
