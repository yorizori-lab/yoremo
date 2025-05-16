package com.yorizori.yoremo.recipes

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.CreateRecipes
import com.yorizori.yoremo.domain.recipes.service.CreateRecipesService
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@SpringBootTest
class CreateRecipesServiceTest {

    @Autowired
    private lateinit var createRecipesService: CreateRecipesService

    @Autowired
    private lateinit var recipesRepository: RecipesRepository

    @Test
    @Transactional
    fun `레시피 생성 성공 테스트`() {
        // given
        // 테스트용 요청 객체 생성
        val request = CreateRecipes.Request(
            title = "김치찌개 테스트",
            description = "매콤한 김치찌개 레시피",
            ingredients = """
                [
                    {"name": "김치", "amount": 300, "unit": "g", "notes": "숙성된 김치 사용"},
                    {"name": "돼지고기", "amount": 200, "unit": "g", "notes": "삼겹살"}
                ]
            """.trimIndent(),
            seasonings = """
                [
                    {"name": "고추장", "amount": 1, "unit": "큰술"},
                    {"name": "간장", "amount": 1, "unit": "큰술"}
                ]
            """.trimIndent(),
            instructions = """
                [
                    {"step_number": 1, "description": "재료를 준비합니다.", "image_url": "step1.jpg"},
                    {"step_number": 2, "description": "김치를 볶습니다.", "image_url": "step2.jpg"}
                ]
            """.trimIndent(),
            // 카테고리 ID는 실제 존재하는 ID로 변경해야 합니다
            categoryTypeId = 1, // 한식 (실제 DB에 존재하는 ID 사용)
            categorySituationId = 11, // 일상 (실제 DB에 존재하는 ID 사용)
            categoryIngredientId = 24, // 돼지고기 (실제 DB에 존재하는 ID 사용)
            categoryMethodId = 38, // 찌개 (실제 DB에 존재하는 ID 사용)
            prepTime = 15,
            cookTime = 30,
            servingSize = 2,
            difficulty = "보통",
            imageUrl = "https://example.com/kimchi-stew.jpg",
            tags = listOf("매콤", "찌개", "돼지고기", "김치")
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

    @Test
    @Transactional
    fun `존재하지 않는 카테고리 ID로 생성 시 예외 발생 테스트`() {
        // given
        val request = CreateRecipes.Request(
            title = "잘못된 카테고리 테스트",
            description = "존재하지 않는 카테고리 ID 테스트",
            ingredients = """[{"name": "테스트", "amount": 100, "unit": "g"}]""",
            seasonings = """[{"name": "소금", "amount": 1, "unit": "작은술"}]""",
            instructions = """[{"step_number": 1, "description": "테스트"}]""",
            categoryTypeId = 9999, // 존재하지 않는 카테고리 ID
            prepTime = 10,
            cookTime = 20,
            servingSize = 1,
            difficulty = "쉬움"
        )

        // when & then
        val exception = assertThrows<ResponseStatusException> {
            createRecipesService.create(request)
        }

        assertEquals("400 BAD_REQUEST \"Category Type not found with id: 9999\"", exception.message)
        println("예외 메시지: ${exception.message}")
    }

    @Test
    @Transactional
    fun `모든 카테고리 ID가 null인 경우 성공 테스트`() {
        // given
        val request = CreateRecipes.Request(
            title = "카테고리 없는 레시피",
            description = "카테고리 없이 생성하는 테스트",
            ingredients = """[{"name": "테스트", "amount": 100, "unit": "g"}]""",
            seasonings = """[{"name": "소금", "amount": 1, "unit": "작은술"}]""",
            instructions = """[{"step_number": 1, "description": "테스트"}]""",
            // 모든 카테고리 ID를 null로 설정
            categoryTypeId = null,
            categorySituationId = null,
            categoryIngredientId = null,
            categoryMethodId = null,
            prepTime = 5,
            cookTime = 10,
            servingSize = 1,
            difficulty = "매우 쉬움"
        )

        // when
        val response = createRecipesService.create(request)

        // then
        assertNotNull(response.recipeId)

        val createdRecipe = recipesRepository.findById(response.recipeId)
        assertNotNull(createdRecipe)
        assertEquals(request.title, createdRecipe?.title)
        assertEquals(request.description, createdRecipe?.description)

        // 카테고리 관계 확인
        assertEquals(null, createdRecipe?.categoryType)
        assertEquals(null, createdRecipe?.categorySituation)
        assertEquals(null, createdRecipe?.categoryIngredient)
        assertEquals(null, createdRecipe?.categoryMethod)

        println("카테고리 없는 레시피 ID: ${response.recipeId}")
    }
}