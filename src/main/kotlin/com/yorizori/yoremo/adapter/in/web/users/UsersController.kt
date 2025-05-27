package com.yorizori.yoremo.adapter.`in`.web.users

import com.yorizori.yoremo.adapter.`in`.web.users.message.CheckEmail
import com.yorizori.yoremo.adapter.`in`.web.users.message.GetMe
import com.yorizori.yoremo.adapter.`in`.web.users.message.Login
import com.yorizori.yoremo.adapter.`in`.web.users.message.ResendVerification
import com.yorizori.yoremo.adapter.`in`.web.users.message.Signup
import com.yorizori.yoremo.adapter.`in`.web.users.message.VerifyEmail
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.service.CheckEmailService
import com.yorizori.yoremo.domain.users.service.EmailVerificationService
import com.yorizori.yoremo.domain.users.service.GetMeService
import com.yorizori.yoremo.domain.users.service.LoginService
import com.yorizori.yoremo.domain.users.service.ResendVerificationService
import com.yorizori.yoremo.domain.users.service.SignupService
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users/v1")
class UsersController(
    private val checkEmailService: CheckEmailService,
    private val signupService: SignupService,
    private val verifyEmailService: EmailVerificationService,
    private val resendVerificationService: ResendVerificationService,
    private val loginService: LoginService,
    private val getMeService: GetMeService
) {

    @PostMapping("/check-email")
    fun checkEmail(
        @RequestBody request: CheckEmail.Request
    ): CheckEmail.Response {
        return checkEmailService.checkEmail(request)
    }

    @PostMapping("/signup")
    fun signup(
        @RequestBody request: Signup.Request
    ): Signup.Response {
        return signupService.signup(request)
    }

    @GetMapping("/verify-email")
    fun verifyEmail(
        @RequestParam token: String
    ): VerifyEmail.Response {
        val request = VerifyEmail.Request(token)
        return verifyEmailService.verifyEmail(request)
    }

    @PostMapping("/resend-verification")
    fun resendVerification(
        @RequestBody request: ResendVerification.Request
    ): ResendVerification.Response {
        return resendVerificationService.resendVerification(request)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: Login.Request,
        session: HttpSession
    ) {
        val user = loginService.login(request)

        session.setAttribute("user", user)
    }

    @PostMapping("/logout")
    fun logout(session: HttpSession) {
        session.invalidate()
    }

    @GetMapping("/me")
    fun getMe(session: HttpSession): GetMe.Response {
        val currentUser = session.getAttribute("user") as? Users
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "로그인이 필요합니다."
            )

        return getMeService.getMe(currentUser)
    }
}
