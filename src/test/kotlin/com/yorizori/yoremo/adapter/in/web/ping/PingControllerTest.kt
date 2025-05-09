package com.yorizori.yoremo.adapter.`in`.web.ping

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(PingController::class)
class PingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun ping() {
        mockMvc.get("/ping")
            .andExpect {
                status { isOk() }
                content { string("pong") }
            }
    }
}
