package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.ResendVerification
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.*

@Service
class ResendVerificationService(
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender
) {
    @Transactional
    fun resendVerification(request: ResendVerification.Request): ResendVerification.Response {
        val user = usersRepository.findByEmail(request.email)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "존재하지 않는 사용자입니다."
            )

        if (user.verificationToken == null) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "이미 인증된 계정입니다."
            )
        }

        val newToken = UUID.randomUUID().toString()
        val newExpiresAt = Instant.now().plusSeconds(5 * 60) // 5분

        val userWithNewToken = user.copy(
            verificationToken = newToken,
            tokenExpiresAt = newExpiresAt
        )
        usersRepository.save(userWithNewToken)

        emailSender.sendVerificationEmail(
            email = userWithNewToken.email,
            token = userWithNewToken.verificationToken!!,
            name = userWithNewToken.name
        )

        return ResendVerification.Response(
            email = userWithNewToken.email
        )
    }
}
