package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class VerifyEmail {
    data class Request(
        val token: String,
        val email: String
    )

    data class Response(
        val email: String
    )
}
