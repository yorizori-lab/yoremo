package com.yorizori.yoremo.adapter.`in`.web.mealrecords

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlan
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlanRecommendation
import com.yorizori.yoremo.domain.mealrecords.service.CreateMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.DeleteMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.MealPlanRecommendationService
import com.yorizori.yoremo.domain.mealrecords.service.UpdateMealRecordsService
import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@YoremoControllerTest
@WebMvcTest(MealRecordsController::class)
class MealRecordsControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var mealPlanRecommendationService: MealPlanRecommendationService

    @MockkBean
    private lateinit var createMealRecordsService: CreateMealRecordsService

    @MockkBean
    private lateinit var updateMealRecordsService: UpdateMealRecordsService

    @MockkBean
    private lateinit var deleteMealRecordsService: DeleteMealRecordsService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return MealRecordsController(
            mealPlanRecommendationService,
            createMealRecordsService,
            updateMealRecordsService,
            deleteMealRecordsService
        )
    }

    @Test
    fun recommendMealPlans() {
        // given
        val request = MealPlanRecommendation.Request(
            question = "다이어트 일주일 식단 추천해주세요",
            sessionId = "session-id-1234"
        )

        every {
            mealPlanRecommendationService.getResponse(any())
        } returns MealPlanRecommendation.Response(
            answer = "다이어트 식단을 추천합니다.",
            mealRecords = listOf(
                MealPlan(
                    day = 1,
                    mealType = "BREAKFAST",
                    foodName = "계란",
                    amount = 1,
                    unit = "개",
                    calories = 140,
                    notes = ""
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/meal-records/v1/recommend")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.answer").value("다이어트 식단을 추천합니다.") }
            .andDo(
                document(
                    "recommend-meal-plans",
                    requestFields(
                        fieldWithPath("question").description("질문"),
                        fieldWithPath("session_id").description("세션 ID")
                    ),
                    responseFields(
                        fieldWithPath("answer").description("답변"),
                        fieldWithPath("mealRecords").description("추천 식단 목록"),
                        fieldWithPath("mealRecords[].day").description("식단 날짜"),
                        fieldWithPath("mealRecords[].mealType").description("식사 종류"),
                        fieldWithPath("mealRecords[].foodName").description("음식 이름"),
                        fieldWithPath("mealRecords[].amount").description("음식 양"),
                        fieldWithPath("mealRecords[].unit").description("음식 단위"),
                        fieldWithPath("mealRecords[].calories").description("칼로리"),
                        fieldWithPath("mealRecords[].notes").description("비고")
                    )
                )
            )
    }
}
