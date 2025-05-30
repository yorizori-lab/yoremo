package com.yorizori.yoremo.adapter.`in`.web.recipes.message

abstract class DeleteRecipes {

    data class PathVariable(val id: Long)

    data class Response(
        val recipeId: Long,
        val foodId: Long
    )
}
