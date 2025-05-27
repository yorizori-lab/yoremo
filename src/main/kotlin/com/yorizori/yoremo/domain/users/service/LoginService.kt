package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.Login
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class LoginService(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun login(request: Login.Request): Users {
        val user = usersRepository.findByEmail(request.email)
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "이메일 또는 비밀번호가 올바르지 않습니다."
            )

        if (user.verificationToken != null) {
            if (user.tokenExpiresAt?.isBefore(Instant.now()) == true) {
                usersRepository.deleteById(user.userId!!)
                throw ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "인증 시간이 만료되어 계정이 삭제되었습니다. 다시 회원가입해주세요."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "이메일 인증이 필요합니다."
                )
            }
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "이메일 또는 비밀번호가 올바르지 않습니다."
            )
        }

        val loggedInUser = user.copy(lastLoginAt = Instant.now())
        usersRepository.save(loggedInUser)

        return loggedInUser
    }
}
