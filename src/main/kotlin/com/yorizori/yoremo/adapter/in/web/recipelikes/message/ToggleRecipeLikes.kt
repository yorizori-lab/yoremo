package com.yorizori.yoremo.adapter.`in`.web.recipelikes.message

abstract class ToggleRecipeLikes {

    data class PathVariable(val recipeId: Long)

    data class Response(
        val isLiked: Boolean,
        val totalLikes: Long
    )
}
