package com.yorizori.yoremo.adapter.`in`.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.yorizori.yoremo.adapter.`in`.web.handler.LoginFailureHandler
import com.yorizori.yoremo.adapter.`in`.web.handler.LoginSuccessHandler
import com.yorizori.yoremo.adapter.`in`.web.users.message.Login
import com.yorizori.yoremo.domain.users.service.LoginService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class LoginFilter(
    private val loginService: LoginService
) : UsernamePasswordAuthenticationFilter() {

    private val objectMapper = ObjectMapper()
    private val loginUrl = "/api/users/v1/login"

    init {
        setFilterProcessesUrl(loginUrl)
        setAuthenticationSuccessHandler(LoginSuccessHandler())
        setAuthenticationFailureHandler(LoginFailureHandler())
    }

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {
        val loginRequest = try {
            objectMapper.readValue(request!!.inputStream, Login::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Invalid login request", e)
        }

        val user = loginService.login(loginRequest.email, loginRequest.password)

        return UsernamePasswordAuthenticationToken(
            user.email,
            null,
            listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        )
    }
}
