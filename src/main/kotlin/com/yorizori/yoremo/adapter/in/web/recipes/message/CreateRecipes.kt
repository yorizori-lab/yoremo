package com.yorizori.yoremo.adapter.`in`.web.recipes.message

import com.yorizori.yoremo.domain.recipes.entity.Recipes

abstract class CreateRecipes {
    data class Request(
        val title: String,
        val description: String? = null,
        val ingredients: List<Recipes.Ingredient>,
        val seasonings: List<Recipes.Seasoning>,
        val instructions: List<Recipes.Instruction>,
        val categoryTypeId: Long? = null,
        val categorySituationId: Long? = null,
        val categoryIngredientId: Long? = null,
        val categoryMethodId: Long? = null,
        val prepTime: Int? = null,
        val cookTime: Int? = null,
        val servingSize: Int? = null,
        val difficulty: Recipes.Difficulty? = null,
        val imageUrl: String? = null,
        val tags: List<String>? = null
    )

    data class Response(
        val recipeId: Long
    )
}
