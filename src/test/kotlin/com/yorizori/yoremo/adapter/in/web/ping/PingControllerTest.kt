package com.yorizori.yoremo.adapter.`in`.web.ping

import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.responseBody
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@YoremoControllerTest
@WebMvcTest(PingController::class)
class PingControllerTest : RestDocsSupport() {

    override fun initController(): Any {
        return PingController()
    }

    @Test
    fun ping() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/ping")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(content().string("pong"))
            .andDo(
                document(
                    "ping",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseBody()
                )
            )
    }
}
