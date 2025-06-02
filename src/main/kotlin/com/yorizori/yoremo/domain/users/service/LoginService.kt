package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun login(email: String, password: String): Users {
        val user = usersRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: $email")

        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        return user
    }
}
