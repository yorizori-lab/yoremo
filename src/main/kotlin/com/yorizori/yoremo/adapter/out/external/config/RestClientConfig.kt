package com.yorizori.yoremo.adapter.out.external.config

import com.yorizori.yoremo.adapter.out.external.jsonplaceholder.JsonPlaceholderApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class RestClientConfig {

    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }

    @Bean
    fun jsonPlaceholderApi(
        @Value("\${adapter.http.json-placeholder.base-url}") baseUrl: String,
        restClient: RestClient
    ): JsonPlaceholderApi {
        return createApiClient(restClient, baseUrl)
    }

    private inline fun <reified T> createApiClient(restClient: RestClient, baseUrl: String): T {
        val customRestClient = restClient.mutate()
            .baseUrl(baseUrl)
            .build()

        val adapter = RestClientAdapter.create(customRestClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(T::class.java)
    }
}
