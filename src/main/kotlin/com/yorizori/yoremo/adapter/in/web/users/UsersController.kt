package com.yorizori.yoremo.adapter.`in`.web.users

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.adapter.`in`.web.users.message.Me
import com.yorizori.yoremo.adapter.`in`.web.users.message.Register
import com.yorizori.yoremo.adapter.`in`.web.users.message.SendVerification
import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.domain.users.service.EmailVerificationService
import com.yorizori.yoremo.domain.users.service.RegisterService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users/v1")
class UsersController(
    private val emailVerificationService: EmailVerificationService,
    private val registerService: RegisterService
) {

    @PostMapping("/send-verification")
    fun sendVerification(
        @RequestBody request: SendVerification.Request
    ): SendVerification.Response {
        return emailVerificationService.sendVerificationCode(request)
    }

    @PostMapping("/verify-email")
    fun verifyEmail(
        @RequestBody request: VerifyEmail.Request
    ): VerifyEmail.Response {
        return emailVerificationService.verifyCode(request)
    }

    @PostMapping("/register")
    fun register(
        @RequestBody request: Register.Request
    ): Register.Response {
        return registerService.register(request)
    }

    @GetMapping("/me")
    fun getCurrentUser(
        @AuthenticationPrincipal yoremoAuth: YoremoAuthentication
    ): Me.Response {
        return Me.Response(
            userId = yoremoAuth.userId,
            email = yoremoAuth.username!!
        )
    }
}
