package com.yorizori.yoremo.adapter.out.external.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig {

    /**
     * Open AI ChatClient
     */
    @Bean
    fun chatClient(
        builder: ChatClient.Builder
    ): ChatClient {
        return builder.build()
    }
}
