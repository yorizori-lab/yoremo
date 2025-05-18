package com.yorizori.yoremo.recipes

import com.yorizori.yoremo.domain.recipes.service.RecipesEmbeddingService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RecipesEmbeddingTest {

    @Autowired
    private lateinit var recipesEmbeddingService: RecipesEmbeddingService

    @Test
    fun testEmbedRecipes() {
        // 실행 전 로그
        println("레시피 임베딩 테스트 시작...")

        // 서비스 실행
        val processedCount = recipesEmbeddingService.embedRecipesFromDatabase()

        // 결과 출력
        println("테스트 완료: 총 $processedCount 개의 레시피가 처리되었습니다.")
    }
}
