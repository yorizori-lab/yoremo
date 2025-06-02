package com.yorizori.yoremo.adapter.`in`.web.users.message

abstract class Login {
    data class Request(
        val email: String,
        val password: String
    )
}
