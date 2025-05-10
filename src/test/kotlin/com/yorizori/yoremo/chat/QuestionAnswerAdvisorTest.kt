package com.yorizori.yoremo.chat

import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class QuestionAnswerAdvisorTest {

    @Autowired
    private lateinit var vectorStore: VectorStore

    @Autowired
    private lateinit var chatModel: ChatModel

    @Test
    fun test() {
        val question = "Where does the adventure of Anacletus and Birba take place?"

        val qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore).build()

        val response = ChatClient.builder(chatModel).build()
            .prompt()
            .advisors(qaAdvisor)
            .user(question)
            .call()
            .content()

        println("Q: $question")
        println("A: $response")
    }
}
