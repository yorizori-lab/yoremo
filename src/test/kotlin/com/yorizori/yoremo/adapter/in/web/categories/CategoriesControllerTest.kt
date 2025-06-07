package com.yorizori.yoremo.adapter.`in`.web.categories

import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.categories.message.ListCategories
import com.yorizori.yoremo.config.FixtureMonkeyUtils.giveMeOne
import com.yorizori.yoremo.config.TestSecurityConfig
import com.yorizori.yoremo.domain.categories.service.ListCategoriesService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CategoriesController::class])
@Import(TestSecurityConfig::class)
class CategoriesControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var listCategoriesService: ListCategoriesService

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
                jsonPath("$.categories[0].category_type")
                    .value(request.categoryType.name)
            )
    }
}
