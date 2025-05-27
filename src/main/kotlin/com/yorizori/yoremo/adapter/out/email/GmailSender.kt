package com.yorizori.yoremo.adapter.out.email

import com.yorizori.yoremo.domain.users.port.EmailSender
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class GmailSender(
    private val javaMailSender: JavaMailSender
) : EmailSender {

    @Value("\${app.frontend.base-url}")
    private lateinit var baseUrl: String

    private val expiryMinutes = 5

    override fun sendVerificationEmail(email: String, token: String, name: String) {
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("✉️ 요레모 이메일 인증")
        helper.setText(createVerificationEmailHtml(name, token), true)

        javaMailSender.send(message)
    }

    override fun sendPasswordResetEmail(email: String, token: String, name: String) {
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("🔐 요레모 비밀번호 재설정")
        helper.setText(createPasswordResetEmailHtml(name, token), true)

        javaMailSender.send(message)
    }

    override fun sendWelcomeEmail(email: String, name: String) {
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("🍳 요레모에 오신 것을 환영합니다!")
        helper.setText(createWelcomeEmailHtml(name), true)

        javaMailSender.send(message)
    }

    private fun createVerificationEmailHtml(name: String, token: String): String {
        val verificationUrl = "$baseUrl/auth/verify-email?token=$token"
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>요레모 이메일 인증</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8f9fa;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    <!-- 헤더 -->
                    <div style="background: linear-gradient(135deg, #ff6b6b, #ffa500); padding: 40px 20px; text-align: center;">
                        <h1 style="color: white; margin: 0; font-size: 28px; font-weight: bold;">🍳 요레모</h1>
                        <p style="color: white; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">요리 레시피 모음집</p>
                    </div>
                    
                    <!-- 본문 -->
                    <div style="padding: 40px 30px;">
                        <h2 style="color: #333; margin: 0 0 20px 0; font-size: 24px;">안녕하세요 ${name}님! 👋</h2>
                        <p style="color: #666; line-height: 1.6; font-size: 16px; margin: 0 0 30px 0;">
                            요레모에 가입해주셔서 감사합니다!<br>
                            아래 버튼을 클릭하여 이메일 인증을 완료해주세요.
                        </p>
                        
                        <!-- 인증 버튼 -->
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="$verificationUrl" 
                               style="display: inline-block; background: linear-gradient(135deg, #ff6b6b, #ffa500); 
                                      color: white; text-decoration: none; padding: 15px 40px; 
                                      border-radius: 50px; font-size: 18px; font-weight: bold; 
                                      box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
                                      transition: all 0.3s ease;">
                                ✉️ 이메일 인증하기
                            </a>
                        </div>
                        
                        <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 8px; padding: 20px; margin: 30px 0;">
                            <p style="color: #856404; margin: 0; font-size: 14px; text-align: center;">
                                ⏰ <strong>중요:</strong> 이 링크는 ${expiryMinutes}분 후 만료됩니다.
                            </p>
                        </div>
                    </div>
                    
                    <!-- 푸터 -->
                    <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #e9ecef;">
                        <p style="color: #6c757d; font-size: 14px; margin: 0;">
                            요레모와 함께 맛있는 요리 여행을 시작해보세요! 🍽️
                        </p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    private fun createPasswordResetEmailHtml(name: String, token: String): String {
        val resetUrl = "$baseUrl/auth/reset-password?token=$token"
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>요레모 비밀번호 재설정</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8f9fa;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    <!-- 헤더 -->
                    <div style="background: linear-gradient(135deg, #6c5ce7, #a29bfe); padding: 40px 20px; text-align: center;">
                        <h1 style="color: white; margin: 0; font-size: 28px; font-weight: bold;">🔐 요레모</h1>
                        <p style="color: white; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">비밀번호 재설정</p>
                    </div>
                    
                    <!-- 본문 -->
                    <div style="padding: 40px 30px;">
                        <h2 style="color: #333; margin: 0 0 20px 0; font-size: 24px;">안녕하세요 ${name}님! 👋</h2>
                        <p style="color: #666; line-height: 1.6; font-size: 16px; margin: 0 0 30px 0;">
                            비밀번호 재설정을 요청하셨습니다.<br>
                            아래 버튼을 클릭하여 새로운 비밀번호를 설정해주세요.
                        </p>
                        
                        <!-- 재설정 버튼 -->
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="$resetUrl" 
                               style="display: inline-block; background: linear-gradient(135deg, #6c5ce7, #a29bfe); 
                                      color: white; text-decoration: none; padding: 15px 40px; 
                                      border-radius: 50px; font-size: 18px; font-weight: bold; 
                                      box-shadow: 0 4px 15px rgba(108, 92, 231, 0.3);
                                      transition: all 0.3s ease;">
                                🔐 비밀번호 재설정하기
                            </a>
                        </div>
                        
                        <div style="background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 8px; padding: 20px; margin: 30px 0;">
                            <p style="color: #721c24; margin: 0; font-size: 14px; text-align: center;">
                                ⏰ <strong>중요:</strong> 이 링크는 ${expiryMinutes}분 후 만료됩니다.
                            </p>
                        </div>
                    </div>
                    
                    <!-- 푸터 -->
                    <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #e9ecef;">
                        <p style="color: #6c757d; font-size: 14px; margin: 0;">
                            요레모 - 안전한 계정 관리 🛡️
                        </p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    private fun createWelcomeEmailHtml(name: String): String {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>요레모에 오신 것을 환영합니다</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8f9fa;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                    <!-- 헤더 -->
                    <div style="background: linear-gradient(135deg, #00b894, #00cec9); padding: 40px 20px; text-align: center;">
                        <h1 style="color: white; margin: 0; font-size: 28px; font-weight: bold;">🎉 환영합니다!</h1>
                        <p style="color: white; margin: 10px 0 0 0; font-size: 16px; opacity: 0.9;">요레모 가족이 되신 것을 축하합니다</p>
                    </div>
                    
                    <!-- 본문 -->
                    <div style="padding: 40px 30px;">
                        <h2 style="color: #333; margin: 0 0 20px 0; font-size: 24px;">${name}님, 환영합니다! 🙌</h2>
                        <p style="color: #666; line-height: 1.6; font-size: 16px; margin: 0 0 30px 0;">
                            <strong>요레모(요리 레시피 모음집)</strong>에 가입해주셔서 정말 감사합니다!<br>
                            이제 다양한 맛있는 레시피들을 탐색하고 공유하실 수 있어요.
                        </p>
                        
                        <!-- 기능 소개 -->
                        <div style="background-color: #f8f9fa; border-radius: 10px; padding: 30px; margin: 30px 0;">
                            <h3 style="color: #333; margin: 0 0 20px 0; font-size: 20px; text-align: center;">✨ 요레모에서 할 수 있는 것들</h3>
                            <ul style="color: #666; line-height: 1.8; font-size: 16px; padding-left: 0; list-style: none;">
                                <li style="margin: 15px 0; padding-left: 30px; position: relative;">
                                    <span style="position: absolute; left: 0; top: 0;">🔍</span>
                                    다양한 요리 레시피 검색하기
                                </li>
                                <li style="margin: 15px 0; padding-left: 30px; position: relative;">
                                    <span style="position: absolute; left: 0; top: 0;">📝</span>
                                    나만의 레시피 등록하기
                                </li>
                                <li style="margin: 15px 0; padding-left: 30px; position: relative;">
                                    <span style="position: absolute; left: 0; top: 0;">❤️</span>
                                    좋아하는 레시피 저장하기
                                </li>
                                <li style="margin: 15px 0; padding-left: 30px; position: relative;">
                                    <span style="position: absolute; left: 0; top: 0;">👨‍🍳</span>
                                    다른 요리사들과 소통하기
                                </li>
                            </ul>
                        </div>
                        
                        <!-- 시작하기 버튼 -->
                        <div style="text-align: center; margin: 40px 0;">
                            <a href="$baseUrl" 
                               style="display: inline-block; background: linear-gradient(135deg, #00b894, #00cec9); 
                                      color: white; text-decoration: none; padding: 15px 40px; 
                                      border-radius: 50px; font-size: 18px; font-weight: bold; 
                                      box-shadow: 0 4px 15px rgba(0, 184, 148, 0.3);
                                      transition: all 0.3s ease;">
                                🍳 요레모 시작하기
                            </a>
                        </div>
                    </div>
                    
                    <!-- 푸터 -->
                    <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #e9ecef;">
                        <p style="color: #6c757d; font-size: 14px; margin: 0;">
                            맛있는 요리와 함께하는 즐거운 시간 되세요! 🍽️✨
                        </p>
                    </div>
                </div>
            </body>
            </body>
            </html>
        """.trimIndent()
    }
}
