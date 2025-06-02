package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.Register
import com.yorizori.yoremo.adapter.out.redis.email.RedisVerificationEmailRepository
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterService(
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender,
    private val redisVerificationEmailRepository: RedisVerificationEmailRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun register(request: Register.Request): Register.Response {
        if (!redisVerificationEmailRepository.isEmailVerified(request.email)) {
            throw IllegalArgumentException("Email is not verified")
        }

        if (usersRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val user = Users(
            email = request.email,
            password = request.password?.let { passwordEncoder.encode(it) },
            name = request.name,
            profileImageUrl = request.profileImageUrl
        )

        val savedUser = usersRepository.save(user)

        redisVerificationEmailRepository.deleteEmailVerified(request.email)

        try {
            emailSender.sendWelcomeEmail(user.email)
        } catch (e: Exception) {
            throw RuntimeException("Failed to send registration email", e)
        }

        return Register.Response(
            email = savedUser.email
        )
    }
}
