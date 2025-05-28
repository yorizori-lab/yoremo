package com.yorizori.yoremo.adapter.`in`.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.yorizori.yoremo.adapter.`in`.web.security.CustomUserPrincipal
import com.yorizori.yoremo.adapter.`in`.web.users.message.Login
import com.yorizori.yoremo.domain.users.port.UsersRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.time.Instant

class LoginAuthenticationFilter(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
    private val objectMapper: ObjectMapper
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/api/users/v1/login", "POST")
) {
    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication? {
        if (request == null || response == null) {
            throw AuthenticationServiceException("Request or Response cannot be null")
        }

        val loginRequest = try {
            objectMapper.readValue(request.inputStream, Login.Request::class.java)
        } catch (e: Exception) {
            throw AuthenticationServiceException("Invalid login request", e)
        }

        val user = usersRepository.findByEmail(loginRequest.email)
            ?: throw AuthenticationServiceException(
                "User not found with email: ${loginRequest.email}"
            )

        if (!user.isEmailVerified) {
            throw BadCredentialsException("이메일 인증이 필요합니다.")
        }

        if (user.password != passwordEncoder.encode(loginRequest.password)) {
            throw BadCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.")
        }

        val loggedInUser = user.copy(lastLoginAt = Instant.now())
        usersRepository.save(loggedInUser)

        val userPrincipal = CustomUserPrincipal(loggedInUser)
        return PreAuthenticatedAuthenticationToken(
            userPrincipal,
            null,
            userPrincipal.authorities
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: jakarta.servlet.FilterChain?,
        authResult: Authentication?
    ) {
        if (request == null || response == null || authResult == null) {
            throw AuthenticationServiceException("Request or Response cannot be null")
        }

        super.successfulAuthentication(request, response, chain, authResult)

        val userPrincipal = authResult.principal as CustomUserPrincipal
        val responseBody = mapOf(
            "success" to true,
            "message" to "로그인 성공",
            "user" to mapOf(
                "userId" to userPrincipal.getUserId(),
                "email" to userPrincipal.getEmail(),
                "name" to userPrincipal.getName()
            )
        )

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_OK
        response.writer.write(objectMapper.writeValueAsString(responseBody))
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        if (request == null || response == null || failed == null) {
            throw AuthenticationServiceException("Request or Response cannot be null")
        }

        val errorResponse = mapOf(
            "success" to false,
            "message" to (failed.message ?: "로그인 실패")
        )

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
