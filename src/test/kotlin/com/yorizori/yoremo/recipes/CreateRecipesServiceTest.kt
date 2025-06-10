package com.yorizori.yoremo.recipes

class CreateRecipesServiceTest
/*

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.domain.foods.port.FoodsRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.recipes.service.CreateRecipesService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class CreateRecipesServiceTest {

    @Autowired
    private lateinit var createRecipesService: CreateRecipesService

    @Autowired
    private lateinit var recipesRepository: RecipesRepository

    @Autowired
    private lateinit var foodsRepository: FoodsRepository

    @Test
    @Transactional
    fun `레시피 생성 성공 테스트`() {
        // given
        // 테스트용 요청 객체 생성
        val request = CreateRecipes.Request(
            title = "김치찌개 테스트",
            description = "매콤한 김치찌개 레시피",
            ingredients = listOf(
                Recipes.Ingredient(
                    name = "김치",
                    amount = 300,
                    unit = "g",
                    notes = "숙성된 김치 사용"
                ),
                Recipes.Ingredient(
                    name = "돼지고기",
                    amount = 200,
                    unit = "g",
                    notes = "삼겹살"
                )
            ),
            seasonings = listOf(
                Recipes.Seasoning(
                    name = "고추장",
                    amount = 1,
                    unit = "큰술"
                ),
                Recipes.Seasoning(
                    name = "간장",
                    amount = 1,
                    unit = "큰술"
                )
            ),
            instructions = listOf(
                Recipes.Instruction(
                    stepNumber = 1,
                    description = "재료를 준비합니다.",
                    imageUrl = "step1.jpg"
                ),
                Recipes.Instruction(
                    stepNumber = 2,
                    description = "김치를 볶습니다.",
                    imageUrl = "step2.jpg"
                )
            ),
            // 카테고리 ID는 실제 존재하는 ID로 변경해야 합니다
            categoryTypeId = 1, // 한식 (실제 DB에 존재하는 ID 사용)
            categorySituationId = 11, // 일상 (실제 DB에 존재하는 ID 사용)
            categoryIngredientId = 24, // 돼지고기 (실제 DB에 존재하는 ID 사용)
            categoryMethodId = 38, // 찌개 (실제 DB에 존재하는 ID 사용)
            prepTime = 15,
            cookTime = 30,
            servingSize = 2,
            difficulty = Recipes.Difficulty.EASY,
            imageUrl = "https://example.com/kimchi-stew.jpg",
            tags = listOf("매콤", "찌개", "돼지고기", "김치"),
            caloriesPer100g = 150L
        )

        // when
        val response = createRecipesService.create(request)

        // then
        assertNotNull(response.recipeId)

        // 생성된 레시피 조회하여 데이터 검증
        val createdRecipe = recipesRepository.findById(response.recipeId)
        assertNotNull(createdRecipe)
        assertEquals(request.title, createdRecipe?.title)
        assertEquals(request.description, createdRecipe?.description)

        println("생성된 레시피 ID: ${response.recipeId}")
        println("생성된 레시피 제목: ${createdRecipe?.title}")
        println("생성된 레시피 설명: ${createdRecipe?.description}")
    }
}
*/
