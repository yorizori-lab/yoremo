package com.yorizori.yoremo.adapter.`in`.web.recipes

class RecipesControllerTest {

}
/*

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.DeleteRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.GetRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.adapter.`in`.web.recipes.message.UpdateRecipes
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.service.CreateRecipesService
import com.yorizori.yoremo.domain.recipes.service.DeleteRecipesService
import com.yorizori.yoremo.domain.recipes.service.GetRecipesService
import com.yorizori.yoremo.domain.recipes.service.ListRecipesService
import com.yorizori.yoremo.domain.recipes.service.UpdateRecipesService
import com.yorizori.yoremo.test.FixtureMonkeyUtils.giveMeOne
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@YoremoControllerTest
@WebMvcTest(RecipesController::class)
class RecipesControllerTest : RestDocsSupport() {

    @MockkBean
    private lateinit var getRecipesService: GetRecipesService

    @MockkBean
    private lateinit var createRecipesService: CreateRecipesService

    @MockkBean
    private lateinit var updateRecipesService: UpdateRecipesService

    @MockkBean
    private lateinit var deleteRecipesService: DeleteRecipesService

    @MockkBean
    private lateinit var searchRecipesService: ListRecipesService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    override fun initController(): Any {
        return RecipesController(
            getRecipesService,
            createRecipesService,
            updateRecipesService,
            deleteRecipesService,
            searchRecipesService
        )
    }

    @Test
    fun listRecipes() {
        // given
        val request = SearchRecipes.Request(
            categoryTypeId = 1L,
            categorySituationId = 11L,
            categoryIngredientId = 23L,
            categoryMethodId = 37L,
            difficulty = Recipes.Difficulty.EASY,
            tags = listOf("매콤", "찌개"),
            page = 0,
            pageSize = 10
        )

        every {
            searchRecipesService.search(any())
        } returns SearchRecipes.Response(
            totalCount = 1,
            recipes = listOf(
                SearchRecipes.ResponseItem(
                    recipeId = 1L,
                    title = "테스트",
                    description = "테스트 레시피",
                    ingredients = listOf(
                        Recipes.Ingredient(
                            name = "테스트",
                            amount = 300,
                            unit = "g",
                            notes = "테스트 재료"
                        )
                    ),
                    seasonings = listOf(
                        Recipes.Seasoning(
                            name = "테스트",
                            amount = 2,
                            unit = "ts"
                        )
                    ),
                    instructions = listOf(
                        Recipes.Instruction(
                            stepNumber = 1,
                            description = "테스트",
                            imageUrl = "https://example.com/test.jpg"
                        )
                    ),
                    categoryType = "한식",
                    categorySituation = "일상식",
                    categoryIngredient = "돼지고기",
                    categoryMethod = "찌개",
                    prepTime = 15,
                    cookTime = 30,
                    servingSize = 2,
                    difficulty = "EASY",
                    imageUrl = "https://example.com/recipe.jpg",
                    tags = listOf("매콤", "찌개", "한식"),
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/recipes/v1/recipes/search")
                .param("categoryTypeId", request.categoryTypeId?.toString() ?: "")
                .param("categorySituationId", request.categorySituationId?.toString() ?: "")
                .param("categoryIngredientId", request.categoryIngredientId?.toString() ?: "")
                .param("categoryMethodId", request.categoryMethodId?.toString() ?: "")
                .param("difficulty", request.difficulty?.name ?: "")
                .param("tags", request.tags?.joinToString(",") ?: "")
                .param("page", request.page.toString())
                .param("pageSize", request.pageSize.toString())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalCount").value(1))
            .andExpect(jsonPath("$.recipes").isArray)
            .andDo(
                document(
                    "recipes-list",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("categoryTypeId").optional().description("카테고리 타입 ID"),
                        parameterWithName("categorySituationId").optional().description(
                            "상황 카테고리 ID"
                        ),
                        parameterWithName("categoryIngredientId").optional().description(
                            "재료 카테고리 ID"
                        ),
                        parameterWithName("categoryMethodId").optional().description("조리법 카테고리 ID"),
                        parameterWithName("difficulty").optional().description("난이도"),
                        parameterWithName("tags").optional().description("태그 목록"),
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("pageSize").optional().description("페이지 크기")
                    ),
                    responseFields(
                        fieldWithPath("totalCount").description("총 레시피 개수"),
                        fieldWithPath("recipes").description("레시피 목록"),
                        fieldWithPath("recipes[].recipeId").description("레시피 ID"),
                        fieldWithPath("recipes[].title").description("레시피 제목"),
                        fieldWithPath("recipes[].description").optional().description("레시피 설명"),
                        fieldWithPath("recipes[].ingredients").description("재료 목록"),
                        fieldWithPath("recipes[].ingredients[].name").description("재료명"),
                        fieldWithPath("recipes[].ingredients[].amount").description("재료 수량"),
                        fieldWithPath("recipes[].ingredients[].unit").description("재료 단위"),
                        fieldWithPath("recipes[].ingredients[].notes").optional().description(
                            "재료 비고"
                        ),
                        fieldWithPath("recipes[].seasonings").description("양념 목록"),
                        fieldWithPath("recipes[].seasonings[].name").description("양념명"),
                        fieldWithPath("recipes[].seasonings[].amount").description("양념 수량"),
                        fieldWithPath("recipes[].seasonings[].unit").description("양념 단위"),
                        fieldWithPath("recipes[].instructions").description("조리 방법"),
                        fieldWithPath("recipes[].instructions[].stepNumber").description("단계 번호"),
                        fieldWithPath("recipes[].instructions[].description").description("조리 설명"),
                        fieldWithPath("recipes[].instructions[].imageUrl").optional().description(
                            "단계별 이미지 URL"
                        ),
                        fieldWithPath("recipes[].ingredients").description("재료 목록"),
                        fieldWithPath("recipes[].seasonings").description("양념 목록"),
                        fieldWithPath("recipes[].instructions").description("조리 방법"),
                        fieldWithPath("recipes[].categoryType").optional().description("카테고리 타입"),
                        fieldWithPath("recipes[].categorySituation").optional().description(
                            "상황 카테고리"
                        ),
                        fieldWithPath("recipes[].categoryIngredient").optional().description(
                            "재료 카테고리"
                        ),
                        fieldWithPath("recipes[].categoryMethod").optional().description(
                            "조리 방법 카테고리"
                        ),
                        fieldWithPath("recipes[].prepTime").optional().description("준비 시간 (분)"),
                        fieldWithPath("recipes[].cookTime").optional().description("조리 시간 (분)"),
                        fieldWithPath("recipes[].servingSize").optional().description("인분 수"),
                        fieldWithPath("recipes[].difficulty").optional().description("난이도"),
                        fieldWithPath("recipes[].imageUrl").optional().description(
                            "이미지 URL"
                        ),
                        fieldWithPath("recipes[].tags").optional().description(
                            "태그 목록"
                        ),
                        fieldWithPath("recipes[].createdAt").description(
                            "생성 일시"
                        ),
                        fieldWithPath("recipes[].updatedAt").description(
                            "수정 일시"
                        )
                    )
                )
            )
    }

    @Test
    fun getRecipes() {
        // given
        val request = giveMeOne<GetRecipes.PathVariable>()

        every {
            getRecipesService.getRecipes(request.id)
        } returns GetRecipes.Response(
            recipeId = request.id,
            title = "테스트",
            description = "테스트 레시피",
            ingredients = listOf(
                Recipes.Ingredient(
                    name = "테스트",
                    amount = 300,
                    unit = "g",
                    notes = "테스트 재료"
                )
            ),
            seasonings = listOf(
                Recipes.Seasoning(
                    name = "테스트",
                    amount = 2,
                    unit = "ts"
                )
            ),
            instructions = listOf(
                Recipes.Instruction(
                    stepNumber = 1,
                    description = "테스트",
                    imageUrl = "https://example.com/test.jpg"
                )
            ),
            categoryType = 1L,
            categorySituation = 11L,
            categoryIngredient = 24L,
            categoryMethod = 38L,
            prepTime = 15,
            cookTime = 30,
            servingSize = 2,
            difficulty = Recipes.Difficulty.EASY,
            imageUrl = "https://example.com/recipe.jpg",
            tags = listOf("매콤", "찌개", "한식"),
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            caloriesPer100g = 150L
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/recipes/v1/recipes/{id}", request.id)
        )
            .andExpect(status().isOk)
            .andExpect { jsonPath("$.recipeId").value(request.id) }
            .andExpect(jsonPath("$.title").value("테스트"))
            .andExpect(jsonPath("$.description").value("테스트 레시피"))
            .andDo(
                document(
                    "recipes-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("레시피 ID")
                    ),
                    responseFields(
                        fieldWithPath("recipeId").description("레시피 ID"),
                        fieldWithPath("title").description("레시피 제목"),
                        fieldWithPath("description").description("레시피 설명"),
                        fieldWithPath("ingredients").description("재료 목록"),
                        fieldWithPath("seasonings").description("양념 목록"),
                        fieldWithPath("instructions").description("조리 방법"),
                        fieldWithPath("categoryType").optional().description("카테고리 타입 ID"),
                        fieldWithPath("categorySituation").optional().description("상황 카테고리 ID"),
                        fieldWithPath("categoryIngredient").optional().description("재료 카테고리 ID"),
                        fieldWithPath("categoryMethod").optional().description("조리 방법 카테고리 ID"),
                        fieldWithPath("prepTime").optional().description("준비 시간 (분)"),
                        fieldWithPath("cookTime").optional().description("조리 시간 (분)"),
                        fieldWithPath("servingSize").optional().description("인분 수"),
                        fieldWithPath("difficulty").optional().description("난이도"),
                        fieldWithPath("imageUrl").optional().description("이미지 URL"),
                        fieldWithPath("tags").optional().description("태그 목록"),
                        fieldWithPath("createdAt").description("생성 일시"),
                        fieldWithPath("updatedAt").description("수정 일시"),
                        fieldWithPath("caloriesPer100g").optional().description("100g당 칼로리"),
                        fieldWithPath("ingredients[].name").description("재료명"),
                        fieldWithPath("ingredients[].amount").description("재료 수량"),
                        fieldWithPath("ingredients[].unit").description("재료 단위"),
                        fieldWithPath("ingredients[].notes").optional().description("재료 비고"),
                        fieldWithPath("seasonings[].name").description("양념명"),
                        fieldWithPath("seasonings[].amount").description("양념 수량"),
                        fieldWithPath("seasonings[].unit").description("양념 단위"),
                        fieldWithPath("instructions[].stepNumber").description("단계 번호"),
                        fieldWithPath("instructions[].description").description("조리 설명"),
                        fieldWithPath("instructions[].imageUrl").optional().description(
                            "단계별 이미지 URL"
                        )
                    )
                )
            )
    }

    @Test
    fun createRecipes() {
        // given
        val request = CreateRecipes.Request(
            title = "김치찌개",
            description = "매콤한 김치찌개",
            ingredients = listOf(
                Recipes.Ingredient(
                    name = "김치",
                    amount = 300,
                    unit = "g",
                    notes = "신김치"
                )
            ),
            seasonings = listOf(
                Recipes.Seasoning(
                    name = "고추장",
                    amount = 2,
                    unit = "큰술"
                )
            ),
            instructions = listOf(
                Recipes.Instruction(
                    stepNumber = 1,
                    description = "김치를 볶고 물을 넣어 끓인다",
                    imageUrl = "https://example.com/step1.jpg"
                )
            ),
            categoryTypeId = 1L,
            categorySituationId = 11L,
            categoryIngredientId = 24L,
            categoryMethodId = 38L,
            prepTime = 15,
            cookTime = 30,
            servingSize = 2,
            difficulty = Recipes.Difficulty.EASY,
            imageUrl = "https://example.com/kimchi.jpg",
            tags = listOf("매콤", "찌개"),
            caloriesPer100g = 150L
        )

        every {
            createRecipesService.create(any())
        } returns CreateRecipes.Response(
            recipeId = 1L
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/recipes/v1/recipes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.recipeId").value(1L))
            .andDo(
                document(
                    "recipes-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("title").description("레시피 제목"),
                        fieldWithPath("description").description("레시피 설명"),
                        fieldWithPath("ingredients[].name").description("재료명"),
                        fieldWithPath("ingredients[].amount").description("재료 수량"),
                        fieldWithPath("ingredients[].unit").description("재료 단위"),
                        fieldWithPath("ingredients[].notes").description("재료 비고"),
                        fieldWithPath("seasonings[].name").description("양념명"),
                        fieldWithPath("seasonings[].amount").description("양념 수량"),
                        fieldWithPath("seasonings[].unit").description("양념 단위"),
                        fieldWithPath("instructions[].step_number").description("단계 번호"),
                        fieldWithPath("instructions[].description").description("조리 설명"),
                        fieldWithPath("instructions[].image_url").description("단계별 이미지"),
                        fieldWithPath("category_type_id").description("카테고리 타입 ID"),
                        fieldWithPath("category_situation_id").description("상황 카테고리 ID"),
                        fieldWithPath("category_ingredient_id").description("재료 카테고리 ID"),
                        fieldWithPath("category_method_id").description("조리법 카테고리 ID"),
                        fieldWithPath("prep_time").description("준비 시간"),
                        fieldWithPath("cook_time").description("조리 시간"),
                        fieldWithPath("serving_size").description("인분 수"),
                        fieldWithPath("difficulty").description("난이도"),
                        fieldWithPath("image_url").description("레시피 이미지"),
                        fieldWithPath("tags").description("태그"),
                        fieldWithPath("calories_per100g").description("100g당 칼로리")
                    ),
                    responseFields(
                        fieldWithPath("recipeId").description(
                            "생성된 레시피 ID"
                        )
                    )
                )
            )
    }

    @Test
    fun updateRecipes() {
        // given
        val pathVariable = giveMeOne<UpdateRecipes.PathVariable>()
        val request = UpdateRecipes.Request(
            title = "김치찌개",
            description = "매콤한 김치찌개",
            ingredients = listOf(
                Recipes.Ingredient(
                    name = "김치",
                    amount = 300,
                    unit = "g",
                    notes = "신김치"
                )
            ),
            seasonings = listOf(
                Recipes.Seasoning(
                    name = "고추장",
                    amount = 2,
                    unit = "큰술"
                )
            ),
            instructions = listOf(
                Recipes.Instruction(
                    stepNumber = 1,
                    description = "김치를 볶고 물을 넣어 끓인다",
                    imageUrl = "https://example.com/step1.jpg"
                )
            ),
            categoryTypeId = 1L,
            categorySituationId = 11L,
            categoryIngredientId = 24L,
            categoryMethodId = 38L,
            prepTime = 15,
            cookTime = 30,
            servingSize = 2,
            difficulty = Recipes.Difficulty.EASY,
            imageUrl = "https://example.com/kimchi.jpg",
            tags = listOf("매콤", "찌개"),
            caloriesPer100g = 150L
        )

        every {
            updateRecipesService.update(any(), any())
        } returns UpdateRecipes.Response(
            recipeId = pathVariable.id
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/recipes/v1/recipes/{id}", pathVariable.id)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.recipeId").value(pathVariable.id))
            .andDo(
                document(
                    "recipes-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("수정할 레시피 ID")
                    ),
                    requestFields(
                        fieldWithPath("title").description("레시피 제목"),
                        fieldWithPath("description").description("레시피 설명"),
                        fieldWithPath("ingredients[].name").description("재료명"),
                        fieldWithPath("ingredients[].amount").description("재료 수량"),
                        fieldWithPath("ingredients[].unit").description("재료 단위"),
                        fieldWithPath("ingredients[].notes").description("재료 비고"),
                        fieldWithPath("seasonings[].name").description("양념명"),
                        fieldWithPath("seasonings[].amount").description("양념 수량"),
                        fieldWithPath("seasonings[].unit").description("양념 단위"),
                        fieldWithPath("instructions[].step_number").description("단계 번호"),
                        fieldWithPath("instructions[].description").description("조리 설명"),
                        fieldWithPath("instructions[].image_url").description("단계별 이미지"),
                        fieldWithPath("category_type_id").description("카테고리 타입 ID"),
                        fieldWithPath("category_situation_id").description("상황 카테고리 ID"),
                        fieldWithPath("category_ingredient_id").description("재료 카테고리 ID"),
                        fieldWithPath("category_method_id").description("조리법 카테고리 ID"),
                        fieldWithPath("prep_time").description("준비 시간"),
                        fieldWithPath("cook_time").description("조리 시간"),
                        fieldWithPath("serving_size").description("인분 수"),
                        fieldWithPath("difficulty").description("난이도"),
                        fieldWithPath("image_url").description("레시피 이미지"),
                        fieldWithPath("tags").description("태그"),
                        fieldWithPath("calories_per100g").description("100g당 칼로리")
                    ),
                    responseFields(
                        fieldWithPath("recipeId").description("수정된 레시피 ID")
                    )
                )
            )
    }

    @Test
    fun deleteRecipes() {
        // given
        val request = giveMeOne<DeleteRecipes.PathVariable>()

        every {
            deleteRecipesService.delete(request.id)
        } returns DeleteRecipes.Response(
            recipeId = request.id
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/api/recipes/v1/recipes/{id}", request.id)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.recipeId").value(request.id))
            .andDo(
                document(
                    "recipes-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("삭제할 레시피 ID")
                    ),
                    responseFields(
                        fieldWithPath("recipeId").description("삭제된 레시피 ID")
                    )
                )
            )
    }
}
*/
