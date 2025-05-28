package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class Register {
    data class Request(
        val email: String,
        val password: String,
        val name: String
    )

    data class Response(
        val email: String
    )
}
