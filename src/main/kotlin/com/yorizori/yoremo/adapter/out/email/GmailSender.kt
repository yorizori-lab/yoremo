package com.yorizori.yoremo.adapter.out.email

import com.yorizori.yoremo.domain.users.port.EmailSender
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class GmailSender(
    private val javaMailSender: JavaMailSender
) : EmailSender {

    override fun sendVerificationEmail(email: String, code: String) {
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("요레모 이메일 인증")
        helper.setText(createVerificationEmailHtml(code), true)

        javaMailSender.send(message)
    }

    override fun sendWelcomeEmail(email: String) {
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("요레모에 오신 것을 환영합니다!")
        helper.setText(createWelcomeEmailHtml(), true)

        javaMailSender.send(message)
    }

    fun createVerificationEmailHtml(code: String): String {
        val content = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #333;">이메일 인증번호</h2>
                <p>안녕하세요! 요레모입니다.</p>
                <p>아래 인증번호를 입력해주세요:</p>
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0;">
                    <h1 style="color: #007bff; margin: 0; font-size: 32px; letter-spacing: 4px;">$code</h1>
                </div>
                <p style="color: #666; font-size: 14px;">
                    • 인증번호는 5분간 유효합니다.<br>
                    • 본인이 요청하지 않았다면 이 메일을 무시해주세요.
                </p>
                <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                <p style="color: #999; font-size: 12px; text-align: center;">
                    © 2025 요레모. All rights reserved.
                </p>
            </div>
        """.trimIndent()

        return content
    }

    fun createWelcomeEmailHtml(): String {
        val content = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2 style="color: #333;">요레모에 오신 것을 환영합니다!</h2>
                <p>안녕하세요! 요레모입니다.</p>
                <p>이제 요레모의 다양한 기능을 이용하실 수 있습니다.</p>
                <p>궁금한 점이 있다면 언제든지 문의해주세요.</p>
                <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                <p style="color: #999; font-size: 12px; text-align: center;">
                    © 2025 요레모. All rights reserved.
                </p>
            </div>
        """.trimIndent()

        return content
    }
}
