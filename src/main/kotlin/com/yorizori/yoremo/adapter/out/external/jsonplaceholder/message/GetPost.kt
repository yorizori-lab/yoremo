package com.yorizori.yoremo.adapter.out.external.jsonplaceholder.message

abstract class GetPost {

    data class PathVariable(val postId: Long)

    data class Response(
        val id: Long,
        val userId: Long,
        val title: String,
        val body: String
    )
}
