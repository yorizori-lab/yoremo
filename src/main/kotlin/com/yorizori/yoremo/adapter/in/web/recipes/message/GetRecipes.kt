package com.yorizori.yoremo.adapter.`in`.web.recipes.message

import com.yorizori.yoremo.domain.recipes.entity.Recipes
import java.time.Instant

abstract class GetRecipes {

    data class PathVariable(val id: Long)

    data class Response(
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
        val updatedAt: Instant,
        val caloriesPer100g: Long? = null,
        val viewCount: Long
    )
}
