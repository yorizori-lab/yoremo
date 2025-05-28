package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.adapter.out.redis.RedisTokenService
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class EmailVerificationService(
    private val usersRepository: UsersRepository,
    private val redisTokenService: RedisTokenService,
    private val emailSender: EmailSender
) {
    @Transactional
    fun verifyEmail(request: VerifyEmail.Request): VerifyEmail.Response {
        if (!redisTokenService.verifyToken(request.email, request.token)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid verification token."
            )
        }

        val user = usersRepository.findByEmail(request.email)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "존재하지 않는 사용자입니다."
            )

        if (user.isEmailVerified) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "이미 인증된 계정입니다."
            )
        }

        val verifiedUser = user.copy(isEmailVerified = true)
        usersRepository.save(verifiedUser)

        redisTokenService.deleteToken(request.email)

        emailSender.sendWelcomeEmail(verifiedUser.email, verifiedUser.name)

        return VerifyEmail.Response(
            email = verifiedUser.email
        )
    }
}
