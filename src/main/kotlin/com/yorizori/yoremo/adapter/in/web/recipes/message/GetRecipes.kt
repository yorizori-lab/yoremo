package com.yorizori.yoremo.adapter.`in`.web.recipes.message

import java.time.Instant

class GetRecipes {

    data class PathVariable(val id: Long)

    data class Response(
        val recipeId: Long,
        val title: String,
        val description: String?,
        val ingredients: String,
        val seasonings: String,
        val instructions: String,
        val categoryType: Long?,
        val categorySituation: Long?,
        val categoryIngredient: Long?,
        val categoryMethod: Long?,
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