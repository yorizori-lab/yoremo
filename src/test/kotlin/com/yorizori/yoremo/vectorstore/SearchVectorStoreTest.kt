package com.yorizori.yoremo.vectorstore

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SearchVectorStoreTest {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var vectorStore: VectorStore

    @Test
    @Disabled
    fun search() {
        val results = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query("Spring")
                .topK(5)
                .build()
        )

        logger.info("Similarity search results: $results")
    }
}
