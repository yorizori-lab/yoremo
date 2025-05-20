package com.yorizori.yoremo.domain.recipes.port

import com.yorizori.yoremo.domain.recipes.entity.Recipes

data class RecipesSearchCommand(
    val categoryTypeId: Long? = null,
    val categorySituationId: Long? = null,
    val categoryIngredientId: Long? = null,
    val categoryMethodId: Long? = null,
    val difficulty: Recipes.Difficulty? = null,
    val tags: List<String>? = null
)
