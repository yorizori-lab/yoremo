package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.SendVerification
import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.adapter.out.redis.email.RedisVerificationEmailRepository
import com.yorizori.yoremo.domain.users.port.EmailSender
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class EmailVerificationService(
    private val redisVerificationEmailRepository: RedisVerificationEmailRepository,
    private val emailSender: EmailSender
) {

    private val duration: Duration = Duration.ofMinutes(5)

    fun sendVerificationCode(
        request: SendVerification.Request
    ): SendVerification.Response {
        return try {
            val code = String.format("%06d", (0..999999).random())

            redisVerificationEmailRepository.setEmailCode(request.email, code, duration)

            emailSender.sendVerificationEmail(request.email, code)

            SendVerification.Response(
                success = true
            )
        } catch (e: Exception) {
            SendVerification.Response(
                success = false
            )
        }
    }

    fun verifyCode(
        request: VerifyEmail.Request
    ): VerifyEmail.Response {
        val storedCode = redisVerificationEmailRepository.getEmailCode(request.email)

        return if (storedCode == request.code) {
            redisVerificationEmailRepository.deleteEmailCode(request.email)
            redisVerificationEmailRepository.setEmailVerified(request.email, duration)
            VerifyEmail.Response(
                success = true
            )
        } else {
            VerifyEmail.Response(
                success = false
            )
        }
    }
}
