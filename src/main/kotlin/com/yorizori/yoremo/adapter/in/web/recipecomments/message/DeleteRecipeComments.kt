package com.yorizori.yoremo.adapter.`in`.web.recipecomments.message

abstract class DeleteRecipeComments {

    data class PathVariable(val commentId: Long)

    data class Response(
        val commentId: Long
    )
}
