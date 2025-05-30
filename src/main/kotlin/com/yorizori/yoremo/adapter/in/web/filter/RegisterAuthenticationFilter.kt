package com.yorizori.yoremo.adapter.`in`.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.yorizori.yoremo.adapter.`in`.web.users.message.Register
import com.yorizori.yoremo.adapter.out.redis.RedisTokenService
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.UUID
import kotlin.collections.emptyList

class RegisterAuthenticationFilter(
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender,
    private val passwordEncoder: PasswordEncoder,
    private val redisTokenService: RedisTokenService,
    private val objectMapper: ObjectMapper
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/api/users/v1/register", "POST")
) {

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {
        if (request == null || response == null) {
            throw IllegalArgumentException("Request or Response cannot be null")
        }

        val registerRequest = try {
            objectMapper.readValue(request.inputStream, Register.Request::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid registration request", e)
        }

        if (usersRepository.existsByEmail(registerRequest.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }

        val encodedPassword = passwordEncoder.encode(registerRequest.password)

        val verificationToken = UUID.randomUUID().toString()

        val user = Users(
            email = registerRequest.email,
            password = encodedPassword,
            name = registerRequest.name,
            isEmailVerified = false
        )

        val savedUser = usersRepository.save(user)

        redisTokenService.saveToken(registerRequest.email, verificationToken)

        emailSender.sendVerificationEmail(
            email = savedUser.email,
            token = verificationToken,
            name = savedUser.name
        )

        return PreAuthenticatedAuthenticationToken(
            "register_success",
            emptyList<GrantedAuthority>()
        )
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: jakarta.servlet.FilterChain?,
        authResult: Authentication?
    ) {
        if (request == null || response == null || authResult == null) {
            throw IllegalArgumentException("Request or Response cannot be null")
        }

        val responseBody = mapOf(
            "success" to true,
            "message" to "회원가입이 완료되었습니다. 이메일 인증을 진행해주세요."
        )

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_CREATED
        response.writer.write(objectMapper.writeValueAsString(responseBody))
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException
    ) {
        if (request == null || response == null) {
            throw IllegalArgumentException("Request or Response cannot be null")
        }

        val errorResponse = mapOf(
            "success" to false,
            "message" to (failed.message ?: "회원가입 실패")
        )

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
