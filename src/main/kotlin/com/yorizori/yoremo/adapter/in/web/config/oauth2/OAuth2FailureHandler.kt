package com.yorizori.yoremo.adapter.`in`.web.oauth2

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class OAuth2FailureHandler : AuthenticationFailureHandler {

    @Value("\${app.frontend.base-url}")
    private lateinit var frontendBaseUrl: String

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val errorMessage = URLEncoder.encode(exception.message ?: "소셜 로그인에 실패했습니다.", "UTF-8")
        response.sendRedirect("$frontendBaseUrl/auth/oauth/error?message=$errorMessage")
    }
}
