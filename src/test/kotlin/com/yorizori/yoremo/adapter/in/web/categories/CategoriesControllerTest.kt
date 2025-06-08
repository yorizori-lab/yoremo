package com.yorizori.yoremo.adapter.`in`.web.categories

import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.categories.message.ListCategories
import com.yorizori.yoremo.domain.categories.service.ListCategoriesService
import com.yorizori.yoremo.test.FixtureMonkeyUtils.giveMeOne
import com.yorizori.yoremo.test.RestDocsSupport
import com.yorizori.yoremo.test.YoremoControllerTest
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
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@YoremoControllerTest
@WebMvcTest(CategoriesController::class)
class CategoriesControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var listCategoriesService: ListCategoriesService

    override fun initController(): Any {
        return CategoriesController(listCategoriesService)
    }

    @Test
    fun listCategories() {
        // given
        val request = giveMeOne<ListCategories.Request>()

        every {
            listCategoriesService.listByType(any())
        } returns ListCategories.Response(
            categories = listOf(
                ListCategories.ResponseItem(
                    categoryId = 1L,
                    name = "테스트",
                    categoryType = request.categoryType,
                    description = "테스트 카테고리"
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/categories/v1/categories")
                .param("categoryType", request.categoryType.name)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.categories").isArray)
            .andExpect(
                jsonPath("$.categories[0].categoryType")
                    .value(request.categoryType.name)
            )
            .andDo(
                document(
                    "category-list",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("categoryType").description("카테고리 타입")
                    ),
                    responseFields(
                        fieldWithPath("categories[].categoryId").description("카테고리 ID"),
                        fieldWithPath("categories[].name").description("카테고리명"),
                        fieldWithPath("categories[].categoryType").description("카테고리 타입"),
                        fieldWithPath("categories[].description").optional().description("설명")
                    )
                )
            )
    }
}
