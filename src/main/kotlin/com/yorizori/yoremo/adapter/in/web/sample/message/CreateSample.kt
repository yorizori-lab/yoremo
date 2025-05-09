package com.yorizori.yoremo.adapter.`in`.web.sample.message

abstract class CreateSample {

    data class Request(val message: String)

    data class Response(val id: Long)
}
