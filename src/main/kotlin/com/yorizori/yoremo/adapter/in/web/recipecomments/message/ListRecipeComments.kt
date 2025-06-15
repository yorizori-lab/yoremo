package com.yorizori.yoremo.adapter.`in`.web.recipecomments.message

import java.time.Instant

abstract class ListRecipeComments {

    data class PathVariable(val recipeId: Long)

    data class Request(
        val page: Int? = 0,
        val pageSize: Int? = 20
    )

    data class Response(
        val totalCount: Int,
        val comments: List<ResponseItem>
    )

    data class ResponseItem(
        val commentId: Long,
        val content: String,
        val userId: Long,
        val userName: String,
        val parentCommentId: Long?,
        val depth: Int,
        val isDeleted: Boolean,
        val childComments: List<ResponseItem>,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
