package com.yorizori.yoremo.recipes

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.SearchRecipes
import com.yorizori.yoremo.domain.categories.entity.Categories
import com.yorizori.yoremo.domain.recipecomments.port.RecipeCommentsRepository
import com.yorizori.yoremo.domain.recipelikes.port.RecipeLikesRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.recipes.service.ListRecipesService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.Instant

class ListRecipesServiceTest {

    private val recipesRepository: RecipesRepository = mockk()
    private val recipeLikesRepository: RecipeLikesRepository = mockk()
    private val recipeCommentsRepository: RecipeCommentsRepository = mockk()

    private val sut: ListRecipesService = ListRecipesService(
        recipesRepository,
        recipeLikesRepository,
        recipeCommentsRepository
    )

    private val categoryType = Categories(1L, "한식", Categories.Type.TYPE)
    private val categorySituation = Categories(2L, "저녁", Categories.Type.SITUATION)
    private val categoryIngredient = Categories(3L, "소고기", Categories.Type.INGREDIENT)
    private val categoryMethod = Categories(4L, "볶음", Categories.Type.METHOD)

    @Test
    fun `레시피를 검색 조건으로 검색하면, 정상적으로 검색된다`() {
        // Given
        val request = SearchRecipes.Request(
            categoryTypeId = 1L,
            categorySituationId = 2L,
            categoryIngredientId = 3L,
            categoryMethodId = 4L,
            difficulty = Recipes.Difficulty.NORMAL,
            tags = listOf("맛있는", "간단한"),
            page = 0,
            pageSize = 10
        )

        val now = Instant.MIN

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

        every {
            recipesRepository.search(any(), any())
        } returns PageImpl(
            listOf(recipe1, recipe2),
            PageRequest.of(request.page!!, request.pageSize!!),
            2
        )

        every {
            recipeLikesRepository.countByRecipeIdIn(listOf(1L, 2L))
        } returns mapOf(1L to 5L, 2L to 3L)

        every {
            recipeCommentsRepository.countByRecipeIdIn(listOf(1L, 2L))
        } returns mapOf(1L to 2L, 2L to 1L)

        // When
        val actual = sut.search(request)

        // Then
        assertEquals(2, actual.recipes.size)
        assertEquals(1L, actual.recipes[0].recipeId)
        assertEquals("소고기 볶음밥", actual.recipes[0].title)
        assertEquals("맛있는 소고기 볶음밥 레시피", actual.recipes[0].description)
        assertEquals("한식", actual.recipes[0].categoryType)
        assertEquals("저녁", actual.recipes[0].categorySituation)
        assertEquals("소고기", actual.recipes[0].categoryIngredient)
        assertEquals("볶음", actual.recipes[0].categoryMethod)
        assertEquals("보통", actual.recipes[0].difficulty)
        assertEquals(listOf("맛있는", "간단한"), actual.recipes[0].tags)

        assertEquals(2, actual.totalCount)
    }

    @Test
    fun `검색결과가 없으면, 빈 페이지를 반환한다`() {
        // Given
        val request = SearchRecipes.Request()
        val pageable = PageRequest.of(0, 10)

        every {
            recipesRepository.search(any(), any())
        } returns PageImpl(emptyList(), pageable, 0)

        every {
            recipeLikesRepository.countByRecipeIdIn(emptyList())
        } returns emptyMap()

        every {
            recipeCommentsRepository.countByRecipeIdIn(emptyList())
        } returns emptyMap()

        // When
        val result = sut.search(request)

        // Then
        assertEquals(0, result.recipes.size)
        assertEquals(0, result.totalCount)
    }
}
