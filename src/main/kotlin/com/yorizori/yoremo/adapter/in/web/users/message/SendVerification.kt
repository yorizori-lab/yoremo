package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class SendVerification {
    data class Request(
        val email: String
    )

    data class Response(
        val success: Boolean
    )
}
