package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class VerifyEmail {
    data class Request(
        val email: String,
        val code: String
    )

    data class Response(
        val success: Boolean
    )
}
