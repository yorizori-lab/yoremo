package com.yorizori.yoremo.adapter.`in`.web.recipecomments.message

import java.time.Instant

abstract class CreateRecipeComment {

    data class PathVariable(val recipeId: Long)

    data class Request(
        val content: String,
        val parentCommentId: Long? = null
    )

    data class Response(
        val commentId: Long,
        val content: String,
        val depth: Int,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
