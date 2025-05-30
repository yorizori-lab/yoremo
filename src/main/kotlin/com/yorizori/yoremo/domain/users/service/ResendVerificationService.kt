package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.ResendVerification
import com.yorizori.yoremo.adapter.out.redis.RedisTokenService
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ResendVerificationService(
    private val usersRepository: UsersRepository,
    private val redisTokenService: RedisTokenService,
    private val emailSender: EmailSender
) {
    @Transactional
    fun resendVerification(request: ResendVerification.Request): ResendVerification.Response {
        val user = usersRepository.findByEmail(request.email)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 사용자입니다."
            )

        if (user.isEmailVerified) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "이미 인증된 계정입니다."
            )
        }

        val newToken = UUID.randomUUID().toString()

        redisTokenService.saveToken(user.email, newToken)

        emailSender.sendVerificationEmail(
            email = user.email,
            token = newToken,
            name = user.name
        )

        return ResendVerification.Response(
            email = user.email
        )
    }
}
