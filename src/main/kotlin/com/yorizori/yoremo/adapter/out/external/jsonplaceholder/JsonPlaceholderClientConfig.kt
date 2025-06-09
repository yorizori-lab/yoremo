package com.yorizori.yoremo.adapter.out.external.jsonplaceholder

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class JsonPlaceholderClientConfig {

    @Bean
    fun jsonPlaceholderHttpServiceProxyFactory(
        @Value("\${adapter.http.json-placeholder.base-url}") baseUrl: String,
        restClient: RestClient
    ): HttpServiceProxyFactory {
        val customRestClient = restClient.mutate()
            .baseUrl(baseUrl)
            .build()

        val adapter = RestClientAdapter.create(customRestClient)

        return HttpServiceProxyFactory
            .builderFor(adapter)
            .build()
    }

    @Bean
    fun jsonPlaceholderClient(
        jsonPlaceholderHttpServiceProxyFactory: HttpServiceProxyFactory
    ): JsonPlaceholderClient {
        return jsonPlaceholderHttpServiceProxyFactory
            .createClient(JsonPlaceholderClient::class.java)
    }
}
