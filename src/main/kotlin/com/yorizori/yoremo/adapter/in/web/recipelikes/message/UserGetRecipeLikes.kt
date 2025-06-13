package com.yorizori.yoremo.adapter.`in`.web.recipelikes.message

import com.yorizori.yoremo.domain.recipes.entity.Recipes
import java.time.Instant

abstract class UserGetRecipeLikes {

    data class Request(
        val page: Int? = 0,
        val pageSize: Int? = 10
    )

    data class Response(
        val totalCount: Int,
        val recipes: List<ResponseItem>
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
        val totalLikes: Long,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
