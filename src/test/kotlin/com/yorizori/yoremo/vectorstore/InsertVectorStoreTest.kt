package com.yorizori.yoremo.vectorstore

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class InsertVectorStoreTest {

    @Autowired
    private lateinit var vectorStore: VectorStore

    @Test
    @Disabled
    fun execute() {
        val documents = listOf(
            Document(
                "Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! " +
                        "Spring AI rocks!! Spring AI rocks!!",
                mapOf("meta1" to "meta1")
            ),
            Document("The World is Big and Salvation Lurks Around the Corner"),
            Document(
                "You walk forward facing the past and you turn back toward the future.",
                mapOf("meta2" to "meta2")
            )
        )

        vectorStore.add(documents)
    }
}
