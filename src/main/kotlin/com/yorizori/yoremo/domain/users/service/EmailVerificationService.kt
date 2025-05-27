package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class EmailVerificationService(
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender
) {
    @Transactional
    fun verifyEmail(request: VerifyEmail.Request): VerifyEmail.Response {
        val user = usersRepository.findByVerificationToken(request.token)
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "유효하지 않은 인증 토큰입니다."
            )

        if (user.tokenExpiresAt?.isBefore(Instant.now()) == true) {
            usersRepository.deleteById(user.userId!!)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "인증 토큰이 만료되었습니다. 다시 시도해주세요."
            )
        }

        val verifiedUser = user.copy(
            verificationToken = null,
            tokenExpiresAt = null
        )
        usersRepository.save(verifiedUser)

        emailSender.sendWelcomeEmail(verifiedUser.email, verifiedUser.name)

        return VerifyEmail.Response(
            email = verifiedUser.email
        )
    }
}
