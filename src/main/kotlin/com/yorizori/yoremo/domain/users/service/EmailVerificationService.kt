package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.SendVerification
import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.adapter.out.redis.RedisUtils
import com.yorizori.yoremo.domain.users.port.EmailSender
import org.springframework.stereotype.Service

@Service
class EmailVerificationService(
    private val redisUtils: RedisUtils,
    private val emailSender: EmailSender
) {

    fun sendVerificationCode(
        request: SendVerification.Request
    ): SendVerification.Response {
        return try {
            val code = String.format("%06d", (0..999999).random())

            redisUtils.setEmailCode(request.email, code)

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
        val storedCode = redisUtils.getEmailCode(request.email)

        return if (storedCode == request.code) {
            redisUtils.deleteEmailCode(request.email)
            redisUtils.setEmailVerified(request.email)
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
