package com.yorizori.yoremo.adapter.`in`.web.recipes.message

import java.time.Instant

class GetRecipes {
    data class Response(
        val recipeId: Int,
        val title: String,
        val description: String?,
        val ingredients: String,
        val seasonings: String,
        val instructions: String,
        val categoryType: Int?,
        val categorySituation: Int?,
        val categoryIngredient: Int?,
        val categoryMethod: Int?,
        val prepTime: Int?,
        val cookTime: Int?,
        val servingSize: Int?,
        val difficulty: String?,
        val imageUrl: String?,
        val tags: String?,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}