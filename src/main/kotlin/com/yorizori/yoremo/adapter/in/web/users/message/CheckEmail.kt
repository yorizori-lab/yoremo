package com.yorizori.yoremo.adapter.`in`.web.users.message

class CheckEmail {
    data class Request(
        val email: String
    )

    data class Response(
        val isAvailable: Boolean
    )
}
