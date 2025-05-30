package com.yorizori.yoremo.adapter.`in`.web.users

import com.yorizori.yoremo.adapter.`in`.web.security.CustomUserPrincipal
import com.yorizori.yoremo.adapter.`in`.web.users.message.CheckEmail
import com.yorizori.yoremo.adapter.`in`.web.users.message.GetMe
import com.yorizori.yoremo.adapter.`in`.web.users.message.ResendVerification
import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.domain.users.service.CheckEmailService
import com.yorizori.yoremo.domain.users.service.EmailVerificationService
import com.yorizori.yoremo.domain.users.service.GetMeService
import com.yorizori.yoremo.domain.users.service.ResendVerificationService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users/v1")
class UsersController(
    private val checkEmailService: CheckEmailService,
    private val verifyEmailService: EmailVerificationService,
    private val resendVerificationService: ResendVerificationService,
    private val getMeService: GetMeService
) {

    @PostMapping("/check-email")
    fun checkEmail(
        @RequestBody request: CheckEmail.Request
    ): CheckEmail.Response {
        return checkEmailService.checkEmail(request)
    }

    @GetMapping("/verify-email")
    fun verifyEmail(
        @RequestParam token: String,
        @RequestParam email: String
    ): VerifyEmail.Response {
        val request = VerifyEmail.Request(token, email)
        return verifyEmailService.verifyEmail(request)
    }

    @PostMapping("/resend-verification")
    fun resendVerification(
        @RequestBody request: ResendVerification.Request
    ): ResendVerification.Response {
        return resendVerificationService.resendVerification(request)
    }

    @GetMapping("/me")
    fun getMe(
        @AuthenticationPrincipal userPrincipal: CustomUserPrincipal
    ): GetMe.Response {
        val currentUser = userPrincipal.getUsers()
        return getMeService.getMe(currentUser)
    }
}
