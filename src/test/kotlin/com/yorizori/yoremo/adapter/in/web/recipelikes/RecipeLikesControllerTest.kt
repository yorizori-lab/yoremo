package com.yorizori.yoremo.adapter.`in`.web.recipelikes

import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.CountRecipeLikes
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.ToggleRecipeLikes
import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.UserGetRecipeLikes
import com.yorizori.yoremo.domain.recipelikes.service.CountRecipeLikesService
import com.yorizori.yoremo.domain.recipelikes.service.ToggleRecipeLikesService
import com.yorizori.yoremo.domain.recipelikes.service.UserGetRecipeLikesService
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
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@WithYoremoUser
@YoremoControllerTest
@WebMvcTest(RecipeLikesController::class)
class RecipeLikesControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var toggleRecipeLikesService: ToggleRecipeLikesService

    @MockkBean
    private lateinit var countRecipeLikesService: CountRecipeLikesService

    @MockkBean
    private lateinit var userGetRecipeLikesService: UserGetRecipeLikesService

    override fun initController(): Any {
        return RecipeLikesController(
            toggleRecipeLikesService,
            countRecipeLikesService,
            userGetRecipeLikesService
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

    @Test
    fun getUserRecipeLikes() {
        every {
            userGetRecipeLikesService.userGetRecipeLikes(any(), any())
        } returns UserGetRecipeLikes.Response(
            totalCount = 5,
            recipes = listOf(
                UserGetRecipeLikes.ResponseItem(
                    recipeId = 1L,
                    title = "Test Recipe",
                    description = "This is a test recipe.",
                    ingredients = emptyList(),
                    seasonings = emptyList(),
                    instructions = emptyList(),
                    categoryType = "Main Dish",
                    categorySituation = "Everyday",
                    categoryIngredient = "Chicken",
                    categoryMethod = "Grilled",
                    prepTime = 10,
                    cookTime = 30,
                    servingSize = 2,
                    difficulty = "Easy",
                    imageUrl = "http://example.com/image.jpg",
                    tags = listOf("tag1", "tag2"),
                    totalLikes = 10L,
                    totalComments = 5L,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/recipe-likes/v1/my-liked-recipes")
                .param("page", "0")
                .param("pageSize", "10")
        )
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.totalCount").value(5) }
            .andExpect { jsonPath("$.recipes[0].recipeId").value(1L) }
            .andExpect { jsonPath("$.recipes[0].title").value("Test Recipe") }
            .andDo(
                document(
                    "recipe-likes/user-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters( // requestParameters → queryParameters
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("pageSize").description("페이지 크기").optional()
                    ),
                    responseFields(
                        fieldWithPath("total_count").description("총 레시피 수"),
                        fieldWithPath("recipes[]").description("레시피 목록"),
                        fieldWithPath("recipes[].recipe_id").description("레시피 ID"),
                        fieldWithPath("recipes[].title").description("레시피 제목"),
                        fieldWithPath("recipes[].description").description("레시피 설명"),
                        fieldWithPath("recipes[].ingredients").description("재료 목록"),
                        fieldWithPath("recipes[].seasonings").description("양념 목록"),
                        fieldWithPath("recipes[].instructions").description("조리 방법"),
                        fieldWithPath("recipes[].category_type").description("카테고리 타입"),
                        fieldWithPath("recipes[].category_situation").description("카테고리 상황"),
                        fieldWithPath("recipes[].category_ingredient").description("카테고리 재료"),
                        fieldWithPath("recipes[].category_method").description("카테고리 방법"),
                        fieldWithPath("recipes[].prep_time").description("준비 시간"),
                        fieldWithPath("recipes[].cook_time").description("조리 시간"),
                        fieldWithPath("recipes[].serving_size").description("인분 수"),
                        fieldWithPath("recipes[].difficulty").description("난이도"),
                        fieldWithPath("recipes[].image_url").description("이미지 URL"),
                        fieldWithPath("recipes[].tags[]").description("태그 목록"),
                        fieldWithPath("recipes[].total_likes").description("총 좋아요 수"),
                        fieldWithPath("recipes[].total_comments").description("총 댓글 수"),
                        fieldWithPath("recipes[].created_at").description("생성 시간"),
                        fieldWithPath("recipes[].updated_at").description("수정 시간")
                    )
                )
            )
    }
}
