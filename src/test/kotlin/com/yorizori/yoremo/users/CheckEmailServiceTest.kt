package com.yorizori.yoremo.users

import com.yorizori.yoremo.adapter.`in`.web.users.message.CheckEmail
import com.yorizori.yoremo.domain.users.port.UsersRepository
import com.yorizori.yoremo.domain.users.service.CheckEmailService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CheckEmailServiceTest {

    private val usersRepository: UsersRepository = mockk()
    private val sut: CheckEmailService = CheckEmailService(usersRepository)

    @Test
    fun `사용 가능한 이메일을 확인하면, 사용 가능하다고 응답한다`() {
        val request = CheckEmail.Request(email = "test@example.com")
        every { usersRepository.existsByEmail("test@example.com") } returns false

        val result = sut.checkEmail(request)

        assertTrue(result.isAvailable)
    }
}
