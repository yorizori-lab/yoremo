package com.yorizori.yoremo.adapter.`in`.web.recipelikes.message

abstract class CountRecipeLikes {

    data class PathVariable(val recipeId: Long)

    data class Response(
        val totalLikes: Long
    )
}
