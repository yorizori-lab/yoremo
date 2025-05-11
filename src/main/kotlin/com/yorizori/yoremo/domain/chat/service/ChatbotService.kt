package com.yorizori.yoremo.domain.chat.service

import com.yorizori.yoremo.adapter.`in`.web.chat.message.ChatMessage
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class ChatbotService(
    chatModel: ChatModel,
    vectorStore: VectorStore,
    chatMemory: ChatMemory
) {

    private val chatClient = ChatClient.builder(chatModel)
        .defaultAdvisors(
            QuestionAnswerAdvisor.builder(vectorStore).build(),
            MessageChatMemoryAdvisor.builder(chatMemory).build()
        )
        .build()

    fun getResponse(request: ChatMessage.Request): ChatMessage.Response {
        val response = chatClient.prompt()
            .user(request.question)
            .advisors { advisor ->
                if (request.sessionId != null) {
                    advisor.param(
                        AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY,
                        request.sessionId
                    )
                }
            }
            .call()
            .content()

        return ChatMessage.Response(answer = response ?: "No response")
    }
}
