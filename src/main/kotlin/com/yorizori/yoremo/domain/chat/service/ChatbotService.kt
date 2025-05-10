package com.yorizori.yoremo.domain.chat.service

import com.yorizori.yoremo.adapter.`in`.web.chat.message.ChatMessage
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class ChatbotService(
    private val vectorStore: VectorStore,
    private val chatModel: ChatModel
) {

    fun getResponse(request: ChatMessage.Request): ChatMessage.Response {
        val qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore).build()

        val response = ChatClient.builder(chatModel).build()
            .prompt()
            .advisors(qaAdvisor)
            .user(request.question)
            .call()
            .content()

        return ChatMessage.Response(answer = response ?: "No response")
    }
}
