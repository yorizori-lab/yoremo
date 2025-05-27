package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.Signup
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.UUID

@Service
class SignupService(
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun signup(request: Signup.Request): Signup.Response {
        if (usersRepository.existsByEmail(request.email)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "이미 존재하는 이메일입니다: ${request.email}"
            )
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        val verificationToken = UUID.randomUUID().toString()
        val tokenExpiresAt = Instant.now().plusSeconds(5 * 60)

        val user = Users(
            email = request.email,
            password = encodedPassword,
            name = request.name,
            verificationToken = verificationToken,
            tokenExpiresAt = tokenExpiresAt
        )

        val savedUser = usersRepository.save(user)

        emailSender.sendVerificationEmail(
            email = savedUser.email,
            token = savedUser.verificationToken!!,
            name = savedUser.name
        )

        return Signup.Response(
            email = savedUser.email
        )
    }
}
