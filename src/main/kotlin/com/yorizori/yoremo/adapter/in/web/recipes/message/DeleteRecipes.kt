package com.yorizori.yoremo.adapter.`in`.web.recipes.message

class DeleteRecipes {

    data class PathVariable(val id: Long)

    data class Response(
        val recipeId: Long
    )
}
