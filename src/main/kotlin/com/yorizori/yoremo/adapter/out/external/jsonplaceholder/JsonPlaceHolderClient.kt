package com.yorizori.yoremo.adapter.out.external.jsonplaceholder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.yorizori.yoremo.adapter.out.external.jsonplaceholder.message.GetPost
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class JsonPlaceHolderClient(
    @Value("\${adapter.http.json-place-holder.base-url}")
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val objectMapper: ObjectMapper
) {
    private val uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)

    fun getPost(request: GetPost.PathVariable): GetPost.Response {
        val response = httpClient.send(
            HttpRequest.newBuilder()
                .uri(
                    uriBuilder
                        .path("/posts/{postId}")
                        .buildAndExpand(request.postId)
                        .toUri()
                )
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        )

        return objectMapper.readValue<GetPost.Response>(response.body())
    }
}
