package com.yorizori.yoremo.adapter.`in`.web.config.security.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class YoremoAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {

    companion object {
        const val AUTHENTICATION_ENTRY_POINT = "/api/users/v1/login"
    }

    init {
        setFilterProcessesUrl(AUTHENTICATION_ENTRY_POINT)
        setAuthenticationSuccessHandler(YoremoAuthenticationSuccessHandler())
        setAuthenticationFailureHandler(YoremoAuthenticationFailureHandler())
    }

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val yoremoLoginRequestRequest = request.toYoremoLoginRequest()

        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                yoremoLoginRequestRequest.email,
                yoremoLoginRequestRequest.password,
            )
        )
    }

    private fun HttpServletRequest.toYoremoLoginRequest(): YoremoLoginRequest {
        return try {
            objectMapper.readValue(this.inputStream, YoremoLoginRequest::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Invalid login request", e)
        }
    }

    data class YoremoLoginRequest(
        val email: String,
        val password: String
    )
}
