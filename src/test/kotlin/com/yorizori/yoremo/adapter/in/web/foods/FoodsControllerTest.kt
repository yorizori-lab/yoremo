package com.yorizori.yoremo.adapter.`in`.web.foods

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.foods.message.Calorie
import com.yorizori.yoremo.domain.foods.service.CalorieCalculatorService
import com.yorizori.yoremo.domain.foods.service.RecommendIngredientsService
import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@YoremoControllerTest
@WebMvcTest(FoodsController::class)
class FoodsControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var calorieCalculatorService: CalorieCalculatorService

    @MockkBean
    private lateinit var recommendIngredientsService: RecommendIngredientsService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return FoodsController(
            calorieCalculatorService,
            recommendIngredientsService
        )
    }

    @Test
    fun calculateCalories() {
        // given
        val request = Calorie.Request(
            title = "김치찌개",
            ingredients = listOf("김치", "돼지고기")
        )

        every {
            calorieCalculatorService.caculateCalories(request)
        } returns Calorie.Response(
            caloriesPer100g = 150
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/foods/v1/calculate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.caloriesPer100g").value(150) }
            .andDo(
                document(
                    "calculate-calories",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("title").description("음식 이름"),
                        fieldWithPath("ingredients").description("재료 목록")
                    ),
                    responseFields(
                        fieldWithPath("calories_per100g").description("100g당 칼로리")
                    )
                )
            )
    }
}
