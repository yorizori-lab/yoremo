package com.yorizori.yoremo.adapter.`in`.web.users.message

class ResendVerification {
    data class Request(
        val email: String
    )

    data class Response(
        val email: String
    )
}
