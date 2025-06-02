package com.yorizori.yoremo.adapter.`in`.web.config.security.authentication

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.context.SecurityContextRepository

class YoremoAuthenticationSuccessHandler(
    private val securityContextRepository: SecurityContextRepository
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        securityContextRepository.saveContext(
            SecurityContextHolder.getContext(),
            request,
            response
        )

        response.status = HttpServletResponse.SC_OK
    }
}
