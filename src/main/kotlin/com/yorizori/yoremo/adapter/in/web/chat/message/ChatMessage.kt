package com.yorizori.yoremo.adapter.`in`.web.chat.message

abstract class ChatMessage {

    data class Request(val question: String, val sessionId: String?)

    data class Response(val answer: String)
}
