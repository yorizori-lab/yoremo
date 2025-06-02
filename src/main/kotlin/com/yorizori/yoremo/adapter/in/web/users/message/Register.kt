package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class Register {
    data class Request(
        val email: String,
        val password: String? = null,
        val name: String,
        val profileImageUrl: String? = null
    )

    data class Response(
        val email: String
    )
}
