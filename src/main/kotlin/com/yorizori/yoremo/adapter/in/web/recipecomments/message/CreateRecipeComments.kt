package com.yorizori.yoremo.adapter.`in`.web.recipecomments.message

abstract class CreateRecipeComments {

    data class PathVariable(val recipeId: Long)

    data class Request(
        val content: String,
        val parentCommentId: Long? = null
    )

    data class Response(
        val commentId: Long
    )
}
