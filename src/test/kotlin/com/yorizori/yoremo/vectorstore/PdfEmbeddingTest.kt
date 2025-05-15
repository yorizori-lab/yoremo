package com.yorizori.yoremo.vectorstore

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.ai.reader.ExtractedTextFormatter
import org.springframework.ai.reader.pdf.PagePdfDocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PdfEmbeddingTest {

    @Autowired
    private lateinit var vectorStore: VectorStore

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val tokenTextSplitter = TokenTextSplitter()

    @Test
    @Disabled
    fun embedPdf() {
        val documents = PagePdfDocumentReader(
            "classpath:/sample.pdf",
            PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(
                    ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(0)
                        .build()
                )
                .withPagesPerDocument(1)
                .build()
        ).read()

        val tokenizedDocuments = tokenTextSplitter.split(documents)

        vectorStore.add(tokenizedDocuments)

        // Perform assertions or further processing on the embedded documents
        logger.info("Embedded documents: $tokenizedDocuments")
    }
}
