package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.CheckEmail
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CheckEmailService(
    private val usersRepository: UsersRepository
) {
    @Transactional(readOnly = true)
    fun checkEmail(request: CheckEmail.Request): CheckEmail.Response {
        val isAvailable = !usersRepository.existsByEmail(request.email)

        return CheckEmail.Response(
            isAvailable = isAvailable
        )
    }
}
