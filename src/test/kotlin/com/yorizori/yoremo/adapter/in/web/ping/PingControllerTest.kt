package com.yorizori.yoremo.adapter.`in`.web.ping

import com.yorizori.yoremo.config.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [PingController::class])
@Import(TestSecurityConfig::class)
class PingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun ping() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/ping")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("pong"))
    }
}
