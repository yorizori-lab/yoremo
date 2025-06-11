package com.yorizori.yoremo.adapter.`in`.web.mealrecords

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.CreateMealRecords
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.DeleteMealRecords
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlan
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlanRecommendation
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.SearchMealRecords
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.UpdateMealRecords
import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import com.yorizori.yoremo.domain.mealrecords.service.CreateMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.DeleteMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.MealPlanRecommendationService
import com.yorizori.yoremo.domain.mealrecords.service.SearchMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.UpdateMealRecordsService
import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
import com.yorizori.yoremo.test.security.WithYoremoUser
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
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
import java.time.Instant

@WithYoremoUser
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

    @MockkBean
    private lateinit var searchMealRecordsService: SearchMealRecordsService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return MealRecordsController(
            mealPlanRecommendationService,
            createMealRecordsService,
            updateMealRecordsService,
            deleteMealRecordsService,
            searchMealRecordsService
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
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("question").description("질문"),
                        fieldWithPath("session_id").description("세션 ID")
                    ),
                    responseFields(
                        fieldWithPath("answer").description("답변"),
                        fieldWithPath("meal_records").description("추천 식단 목록"),
                        fieldWithPath("meal_records[].day").description("식단 날짜"),
                        fieldWithPath("meal_records[].meal_type").description("식사 종류"),
                        fieldWithPath("meal_records[].food_name").description("음식 이름"),
                        fieldWithPath("meal_records[].amount").description("음식 양"),
                        fieldWithPath("meal_records[].unit").description("음식 단위"),
                        fieldWithPath("meal_records[].calories").description("칼로리"),
                        fieldWithPath("meal_records[].notes").description("비고")
                    )
                )
            )
    }

    @Test
    fun createMealRecords() {
        // given
        val request = CreateMealRecords.Request(
            mealPlans = listOf(
                MealPlan(
                    day = 1,
                    mealType = "BREAKFAST",
                    foodName = "계란",
                    amount = 1,
                    unit = "개",
                    calories = 140,
                    notes = ""
                )
            ),
            startDate = Instant.now()
        )

        every {
            createMealRecordsService.create(any(), any())
        } returns CreateMealRecords.Response(success = true)

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/meal-records/v1/meal-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "create-meal-records",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("meal_plans").description("식단 계획 목록"),
                        fieldWithPath("meal_plans[].day").description("식단 날짜"),
                        fieldWithPath("meal_plans[].meal_type").description("식사 종류"),
                        fieldWithPath("meal_plans[].food_name").description("음식 이름"),
                        fieldWithPath("meal_plans[].amount").description("음식 양"),
                        fieldWithPath("meal_plans[].unit").description("음식 단위"),
                        fieldWithPath("meal_plans[].calories").description("칼로리"),
                        fieldWithPath("meal_plans[].notes").description("비고"),
                        fieldWithPath("start_date").description("식단 시작 날짜")
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부")
                    )
                )
            )
    }

    @Test
    fun updateMealRecords() {
        // given
        val request = UpdateMealRecords.Request(
            mealRecords = listOf(
                UpdateMealRecords.UpdateMealRecord(
                    recordId = 1L,
                    mealDate = Instant.now(),
                    foodName = "계란",
                    mealType = MealRecords.MealType.BREAKFAST,
                    amount = 1,
                    unit = "개",
                    totalCalories = 140,
                    notes = "아침식사"
                )
            )
        )

        every {
            updateMealRecordsService.update(any(), any())
        } returns UpdateMealRecords.Response(success = true)

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/meal-records/v1/meal-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "update-meal-records",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("meal_records").description("식사 기록 목록"),
                        fieldWithPath("meal_records[].record_id").description("기록 ID"),
                        fieldWithPath("meal_records[].meal_date").description("식사 날짜"),
                        fieldWithPath("meal_records[].food_name").description("음식 이름"),
                        fieldWithPath("meal_records[].meal_type").description("식사 종류"),
                        fieldWithPath("meal_records[].amount").description("음식 양"),
                        fieldWithPath("meal_records[].unit").description("음식 단위"),
                        fieldWithPath("meal_records[].total_calories").description("총 칼로리"),
                        fieldWithPath("meal_records[].notes").description("비고")
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부")
                    )
                )
            )
    }

    @Test
    fun deleteMealRecords() {
        // given
        val recordIds = DeleteMealRecords.Request(
            recordIds = listOf(1L, 2L, 3L)
        )

        every {
            deleteMealRecordsService.delete(any(), any())
        } returns DeleteMealRecords.Response(success = true)

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/api/meal-records/v1/meal-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordIds))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "delete-meal-records",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("record_ids").description("삭제할 기록 ID 목록")
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공 여부")
                    )
                )
            )
    }

    @Test
    fun searchMealRecords() {
        // given
        val request = SearchMealRecords.Request(
            startDate = Instant.now(),
            endDate = Instant.now().plusSeconds(86400)
        )

        every {
            searchMealRecordsService.search(any(), any())
        } returns SearchMealRecords.Response(
            totalCount = 1,
            mealRecords = listOf(
                SearchMealRecords.ResponseItem(
                    recordId = 1L,
                    mealDate = Instant.now().toString(),
                    foodName = "계란",
                    mealType = MealRecords.MealType.BREAKFAST,
                    amount = 1,
                    unit = "개",
                    totalCalories = 140,
                    notes = "아침식사"
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/meal-records/v1/meal-records/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.meal_records[0].food_name").value("계란"))
            .andDo(
                document(
                    "search-meal-records",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("start_date").description("검색 시작 날짜"),
                        fieldWithPath("end_date").description("검색 종료 날짜")
                    ),
                    responseFields(
                        fieldWithPath("total_count").description("총 기록 수"),
                        fieldWithPath("meal_records").description("식사 기록 목록"),
                        fieldWithPath("meal_records[].record_id").description("기록 ID"),
                        fieldWithPath("meal_records[].meal_date").description("식사 날짜"),
                        fieldWithPath("meal_records[].food_name").description("음식 이름"),
                        fieldWithPath("meal_records[].meal_type").description("식사 종류"),
                        fieldWithPath("meal_records[].amount").description("음식 양"),
                        fieldWithPath("meal_records[].unit").description("음식 단위"),
                        fieldWithPath("meal_records[].total_calories").description("총 칼로리"),
                        fieldWithPath("meal_records[].notes").description("비고")
                    )
                )
            )
    }
}
