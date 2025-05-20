package com.yorizori.yoremo.recipes

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.domain.categories.entity.Categories
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesSearchCommand
import com.yorizori.yoremo.domain.recipes.service.ListRecipesService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Instant

class ListRecipesServiceTest {

    // 필요한 Mock 객체들을 선언
    private lateinit var recipesRepository: RecipesRepository
    private lateinit var listRecipesService: ListRecipesService

    // 테스트 데이터
    private val now = Instant.now()
    private val categoryType = Categories(1L, "한식", Categories.Type.TYPE)
    private val categorySituation = Categories(2L, "저녁", Categories.Type.SITUATION)
    private val categoryIngredient = Categories(3L, "소고기", Categories.Type.INGREDIENT)
    private val categoryMethod = Categories(4L, "볶음", Categories.Type.METHOD)

    @BeforeEach
    fun setUp() {
        // Mock 객체 초기화
        recipesRepository = mockk()
        listRecipesService = ListRecipesService(recipesRepository)
    }

    @Test
    fun `레시피 검색 성공 테스트`() {
        // Given
        val request = SearchRecipes.Request(
            categoryTypeId = 1L,
            categorySituationId = 2L,
            categoryIngredientId = 3L,
            categoryMethodId = 4L,
            difficulty = Recipes.Difficulty.NORMAL,
            tags = listOf("맛있는", "간단한")
        )

        val pageable = PageRequest.of(0, 10)

        // mockk를 사용하여 Recipes 객체 목킹
        val recipe1 = mockk<Recipes> {
            every { recipeId } returns 1L
            every { title } returns "소고기 볶음밥"
            every { description } returns "맛있는 소고기 볶음밥 레시피"
            every { ingredients } returns listOf(
                Recipes.Ingredient("재료1", 100, "g", null),
                Recipes.Ingredient("재료2", 200, "g", "참고사항")
            )
            every { seasonings } returns listOf(
                Recipes.Seasoning("양념1", 1, "큰술"),
                Recipes.Seasoning("양념2", 2, "작은술")
            )
            every { instructions } returns listOf(
                Recipes.Instruction(1, "첫 번째 단계", null),
                Recipes.Instruction(2, "두 번째 단계", "image-url.jpg")
            )
            every { categoryType } returns this@ListRecipesServiceTest.categoryType
            every { categorySituation } returns this@ListRecipesServiceTest.categorySituation
            every { categoryIngredient } returns this@ListRecipesServiceTest.categoryIngredient
            every { categoryMethod } returns this@ListRecipesServiceTest.categoryMethod
            every { prepTime } returns 10
            every { cookTime } returns 20
            every { servingSize } returns 2
            every { difficulty } returns Recipes.Difficulty.NORMAL
            every { imageUrl } returns "http://example.com/image.jpg"
            every { tags } returns listOf("맛있는", "간단한")
            every { createdAt } returns now
            every { updatedAt } returns now
        }

        val recipe2 = mockk<Recipes> {
            every { recipeId } returns 2L
            every { title } returns "김치 볶음밥"
            every { description } returns "맛있는 김치 볶음밥 레시피"
            every { ingredients } returns listOf(
                Recipes.Ingredient("재료1", 100, "g", null),
                Recipes.Ingredient("재료2", 200, "g", "참고사항")
            )
            every { seasonings } returns listOf(
                Recipes.Seasoning("양념1", 1, "큰술"),
                Recipes.Seasoning("양념2", 2, "작은술")
            )
            every { instructions } returns listOf(
                Recipes.Instruction(1, "첫 번째 단계", null),
                Recipes.Instruction(2, "두 번째 단계", "image-url.jpg")
            )
            every { categoryType } returns this@ListRecipesServiceTest.categoryType
            every { categorySituation } returns this@ListRecipesServiceTest.categorySituation
            every { categoryIngredient } returns this@ListRecipesServiceTest.categoryIngredient
            every { categoryMethod } returns this@ListRecipesServiceTest.categoryMethod
            every { prepTime } returns 10
            every { cookTime } returns 20
            every { servingSize } returns 2
            every { difficulty } returns Recipes.Difficulty.NORMAL
            every { imageUrl } returns "http://example.com/image.jpg"
            every { tags } returns listOf("맛있는", "간단한")
            every { createdAt } returns now
            every { updatedAt } returns now
        }

        val recipesList = listOf(recipe1, recipe2)
        val recipesPage = PageImpl(recipesList, pageable, 2)

        // RecipesRepository의 search 메서드가 호출될 때 반환할 값 설정
        every {
            recipesRepository.search(
                match { command ->
                    command.categoryTypeId == request.categoryTypeId &&
                        command.categorySituationId == request.categorySituationId &&
                        command.categoryIngredientId == request.categoryIngredientId &&
                        command.categoryMethodId == request.categoryMethodId &&
                        command.difficulty == request.difficulty &&
                        command.tags == request.tags
                },
                pageable
            )
        } returns recipesPage

        // When
        val result = listRecipesService.search(request, pageable)

        // Then
        // 결과 검증
        assertEquals(2, result.recipes.size)
        assertEquals(1L, result.recipes[0].recipeId)
        assertEquals("소고기 볶음밥", result.recipes[0].title)
        assertEquals("맛있는 소고기 볶음밥 레시피", result.recipes[0].description)
        assertEquals("한식", result.recipes[0].categoryType)
        assertEquals("저녁", result.recipes[0].categorySituation)
        assertEquals("소고기", result.recipes[0].categoryIngredient)
        assertEquals("볶음", result.recipes[0].categoryMethod)
        assertEquals("보통", result.recipes[0].difficulty)
        assertEquals(listOf("맛있는", "간단한"), result.recipes[0].tags)

        assertEquals(2, result.totalElements)
        assertEquals(1, result.totalPages)
        assertEquals(0, result.number)
        assertEquals(10, result.size)
        assertEquals(true, result.first)
        assertEquals(true, result.last)
        assertEquals(false, result.empty)

        // recipesRepository.search가 올바른 인자로 호출되었는지 검증
        verify {
            recipesRepository.search(
                any<RecipesSearchCommand>(),
                pageable
            )
        }
    }

    @Test
    fun `검색 결과가 없을 때 빈 페이지 반환 테스트`() {
        // Given
        val request = SearchRecipes.Request(
            categoryTypeId = 999L // 존재하지 않는 카테고리 ID
        )

        val pageable = PageRequest.of(0, 10)
        val emptyList = listOf<Recipes>()
        val emptyPage = PageImpl(emptyList, pageable, 0)

        // RecipesRepository의 search 메서드가 빈 페이지를 반환하도록 설정
        every {
            recipesRepository.search(any(), pageable)
        } returns emptyPage

        // When
        val result = listRecipesService.search(request, pageable)

        // Then
        assertEquals(0, result.recipes.size)
        assertEquals(0, result.totalElements)
        assertEquals(0, result.totalPages)
        assertEquals(0, result.number)
        assertEquals(10, result.size)
        assertEquals(true, result.first)
        assertEquals(true, result.last)
        assertEquals(true, result.empty)

        verify { recipesRepository.search(any(), pageable) }
    }

    @Test
    fun `페이징 처리 테스트`() {
        // Given
        val request = SearchRecipes.Request()
        val pageable = PageRequest.of(1, 5) // 두 번째 페이지, 페이지당 5개

        // 페이징 테스트용 레시피 목 객체들 생성
        val recipesList = (1..5).map { i ->
            mockk<Recipes> {
                every { recipeId } returns (i + 5).toLong()
                every { title } returns "레시피 ${i + 5}"
                every { description } returns "설명 ${i + 5}"
                every { ingredients } returns listOf(Recipes.Ingredient("재료1", 100, "g", null))
                every { seasonings } returns listOf(Recipes.Seasoning("양념1", 1, "큰술"))
                every { instructions } returns listOf(Recipes.Instruction(1, "단계 1", null))
                every { categoryType } returns this@ListRecipesServiceTest.categoryType
                every { categorySituation } returns this@ListRecipesServiceTest.categorySituation
                every { categoryIngredient } returns this@ListRecipesServiceTest.categoryIngredient
                every { categoryMethod } returns this@ListRecipesServiceTest.categoryMethod
                every { prepTime } returns 10
                every { cookTime } returns 20
                every { servingSize } returns 2
                every { difficulty } returns Recipes.Difficulty.EASY
                every { imageUrl } returns "http://example.com/image.jpg"
                every { tags } returns listOf("태그${i}")
                every { createdAt } returns now
                every { updatedAt } returns now
            }
        }

        val totalElements = 15L // 총 15개의 레시피가 있다고 가정
        val recipesPage = PageImpl(recipesList, pageable, totalElements)

        every {
            recipesRepository.search(any(), pageable)
        } returns recipesPage

        // When
        val result = listRecipesService.search(request, pageable)

        // Then
        assertEquals(5, result.recipes.size)
        assertEquals(6L, result.recipes[0].recipeId) // 두 번째 페이지의 첫 번째 항목은 6번 레시피
        assertEquals(15, result.totalElements)
        assertEquals(3, result.totalPages) // 5개씩 15개 항목 = 3페이지
        assertEquals(1, result.number) // 페이지 번호 (0부터 시작)
        assertEquals(5, result.size)
        assertEquals(false, result.first) // 첫 페이지가 아님
        assertEquals(false, result.last) // 마지막 페이지도 아님
        assertEquals(false, result.empty)

        verify { recipesRepository.search(any(), pageable) }
    }
}
