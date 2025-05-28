package com.yorizori.yoremo.adapter.`in`.web.constant

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class YoremoHttpUrl(
    @Value("\${app.frontend.base-url}")
    val frontendBaseUrl: String
) {

    val oAuthSuccessUrl = "$frontendBaseUrl/auth/oauth/success"

    fun oAuthFailureUrl(message: String): String {
        return UriComponentsBuilder
            .fromUriString("$frontendBaseUrl/auth/oauth/error")
            .queryParam("message", message)
            .build()
            .toUriString()
    }

    fun emailVerificationUrl(token: String, email: String): String {
        return UriComponentsBuilder
            .fromUriString("$frontendBaseUrl/auth/verify-email")
            .queryParam("token", token)
            .queryParam("email", email)
            .build()
            .toUriString()
    }

    fun passwordResetUrl(token: String, email: String): String {
        return UriComponentsBuilder
            .fromUriString("$frontendBaseUrl/auth/reset-password")
            .queryParam("token", token)
            .queryParam("email", email)
            .build()
            .toUriString()
    }
}
