package com.yorizori.yoremo.adapter.`in`.web.recipecomments.message

abstract class UpdateRecipeComments {

    data class PathVariable(val commentId: Long)

    data class Request(
        val content: String
    )

    data class Response(
        val commentId: Long
    )
}
