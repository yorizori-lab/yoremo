package com.yorizori.yoremo.adapter.`in`.web.users.message

class Signup {
    data class Request(
        val email: String,
        val password: String,
        val name: String
    )

    data class Response(
        val email: String
    )
}
