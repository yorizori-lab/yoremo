package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class Me {
    data class Response(
        val userId: Long,
        val email: String
    )
}
