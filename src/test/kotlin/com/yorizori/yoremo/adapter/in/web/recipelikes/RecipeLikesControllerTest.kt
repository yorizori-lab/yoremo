package com.yorizori.yoremo.adapter.`in`.web.recipelikes

import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.CountRecipeLikes
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.ToggleRecipeLikes
import com.yorizori.yoremo.domain.recipelikes.service.CountRecipeLikesService
import com.yorizori.yoremo.domain.recipelikes.service.ToggleRecipeLikesService
import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
import com.yorizori.yoremo.test.security.WithYoremoUser
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithYoremoUser
@YoremoControllerTest
@WebMvcTest(RecipeLikesController::class)
class RecipeLikesControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var toggleRecipeLikesService: ToggleRecipeLikesService

    @MockkBean
    private lateinit var countRecipeLikesService: CountRecipeLikesService

    override fun initController(): Any {
        return RecipeLikesController(
            toggleRecipeLikesService,
            countRecipeLikesService
        )
    }

    @Test
    fun toggleRecipeLikes() {
        // given
        val recipeId = 1L

        every {
            toggleRecipeLikesService.toggle(any(), any())
        } returns ToggleRecipeLikes.Response(
            isLiked = true,
            totalLikes = 10L
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/recipe-likes/v1/{recipeId}/toggle", recipeId)
        )
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.isLiked").value(true) }
            .andExpect { jsonPath("$.totalLikes").value(10L) }
            .andDo(
                document(
                    "recipe-likes/toggle",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("recipeId").description("좋아요를 토글할 레시피 ID")
                    ),
                    responseFields(
                        fieldWithPath("is_liked").description("좋아요 여부"),
                        fieldWithPath("total_likes").description("총 좋아요 수")
                    )
                )
            )
    }

    @Test
    fun countRecipeLikes() {
        // given
        val recipeId = 1L

        every {
            countRecipeLikesService.count(any())
        } returns CountRecipeLikes.Response(
            totalLikes = 10L
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/recipe-likes/v1/{recipeId}/count", recipeId)
        )
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.totalLikes").value(10L) }
            .andDo(
                document(
                    "recipe-likes-count",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("recipeId").description("좋아요를 토글할 레시피 ID")
                    ),
                    responseFields(
                        fieldWithPath("total_likes").description("총 좋아요 수")
                    )
                )
            )
    }
}
