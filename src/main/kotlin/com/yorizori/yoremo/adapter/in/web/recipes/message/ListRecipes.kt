package com.yorizori.yoremo.adapter.`in`.web.recipes.message

import com.yorizori.yoremo.domain.recipes.entity.Recipes
import java.time.Instant

class ListRecipes {
    data class Request(
        val categoryTypeId: Long? = null,
        val categorySituationId: Long? = null,
        val categoryIngredientId: Long? = null,
        val categoryMethodId: Long? = null,
        val difficulty: String? = null,
        val tags: List<String>? = null
    )

    data class Response(
        val recipes: List<ResponseItem>
    )

    data class ResponseItem(
        val recipeId: Long,
        val title: String,
        val description: String?,
        val ingredients: List<Recipes.Ingredient>,
        val seasonings: List<Recipes.Seasoning>,
        val instructions: List<Recipes.Instruction>,
        val categoryType: Long?,
        val categorySituation: Long?,
        val categoryIngredient: Long?,
        val categoryMethod: Long?,
        val prepTime: Int?,
        val cookTime: Int?,
        val servingSize: Int?,
        val difficulty: Recipes.Difficulty?,
        val imageUrl: String?,
        val tags: List<String>?,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
