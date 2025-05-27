package com.yorizori.yoremo.adapter.`in`.web.users.message

class VerifyEmail {
    data class Request(
        val token: String
    )

    data class Response(
        val email: String
    )
}
