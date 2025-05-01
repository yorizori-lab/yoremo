package com.yorizori.yoremo.application.message.sample

abstract class CreateSample {

    data class Request(val message: String)

    data class Response(val id: Long)
}
