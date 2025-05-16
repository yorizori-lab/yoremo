package com.yorizori.yoremo.adapter.`in`.web.recipes.message

class CreateRecipes {
    data class Request(
        val title: String,
        val description: String? = null,
        val ingredients: String,
        val seasonings: String,
        val instructions: String,
        val categoryTypeId: Long? = null,
        val categorySituationId: Long? = null,
        val categoryIngredientId: Long? = null,
        val categoryMethodId: Long? = null,
        val prepTime: Int? = null,
        val cookTime: Int? = null,
        val servingSize: Int? = null,
        val difficulty: String? = null,
        val imageUrl: String? = null,
        val tags: List<String>? = null
    )

    data class Response(
        val recipeId: Long
    )
}