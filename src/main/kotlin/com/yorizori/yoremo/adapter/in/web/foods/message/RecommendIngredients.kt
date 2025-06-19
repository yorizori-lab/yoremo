package com.yorizori.yoremo.adapter.`in`.web.foods.message

import com.yorizori.yoremo.domain.recipes.entity.Recipes

abstract class RecommendIngredients {
    data class Request(
        val ingredients: List<String>,
        val categoryType: String? = null,
        val categoryMethod: String? = null,
        val categorySituation: String? = null,
        val categoryIngredient: String? = null
    )

    data class Response(
        val recommendations: List<RecommendationItem>
    )

    data class RecommendationItem(
        val recipeId: Long,
        val title: String,
        val description: String?,
        val imageUrl: String?,
        val ingredients: List<Recipes.Ingredient>,
        val seasonings: List<Recipes.Seasoning>,
        val prepTime: Int?,
        val cookTime: Int?,
        val servingSize: Int?,
        val difficulty: String?,
        val tags: List<String>?,
        val caloriesPer100g: Long?
    )
}
