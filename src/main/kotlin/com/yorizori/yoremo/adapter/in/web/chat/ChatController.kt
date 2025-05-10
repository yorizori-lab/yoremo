package com.yorizori.yoremo.adapter.`in`.web.chat

import com.yorizori.yoremo.adapter.`in`.web.chat.message.ChatMessage
import com.yorizori.yoremo.domain.chat.service.ChatbotService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat")
class ChatController(
    private val chatbotService: ChatbotService
) {

    @PostMapping("/message")
    fun processMessage(
        @RequestBody request: ChatMessage.Request
    ): ChatMessage.Response {
        return chatbotService.getResponse(request)
    }
}
