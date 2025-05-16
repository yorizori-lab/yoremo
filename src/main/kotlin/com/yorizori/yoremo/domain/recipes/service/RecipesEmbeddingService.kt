package com.yorizori.yoremo.domain.recipes.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yorizori.yoremo.adapter.out.persistence.recipes.RecipesJpaRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipesEmbeddingService(
    private val vectorStore: VectorStore,
    private val recipesJpaRepository: RecipesJpaRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val tokenTextSplitter = TokenTextSplitter()
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Transactional
    fun embedRecipesFromDatabase(pageSize: Int = 100): Int {
        logger.info("레시피 임베딩 시작...")

        // JPA 리포지토리에서 레시피 데이터 가져오기 (페이징 적용)
        var pageNumber = 0
        var totalProcessed = 0

        var page = recipesJpaRepository.findAll(PageRequest.of(pageNumber, pageSize))

        while (page.hasContent()) {
            val recipesList = page.content
            totalProcessed += recipesList.size

            logger.info("페이지 $pageNumber: ${recipesList.size}개 레시피 처리 중...")

            // 레시피를 문서로 변환
            val documents = recipesList.map { recipes ->
                convertRecipesToDocument(recipes)
            }

            // 문서 분할 - 안전한 호출 추가 및 빈 리스트 대체
            val tokenizedDocuments = tokenTextSplitter.apply(documents)
            val documentsToAdd = tokenizedDocuments ?: emptyList()

            logger.info("페이지 $pageNumber: ${documentsToAdd.size}개의 문서로 분할")

            if (documentsToAdd.isNotEmpty()) {
                // 벡터 스토어에 추가
                vectorStore.add(documentsToAdd)
                logger.info("페이지 $pageNumber: 벡터 스토어에 문서 추가 완료")
            } else {
                logger.warn("페이지 $pageNumber: 분할된 문서가 없습니다")
            }

            // 다음 페이지로 이동
            pageNumber++
            page = recipesJpaRepository.findAll(PageRequest.of(pageNumber, pageSize))
        }

        logger.info("레시피 임베딩 완료: 총 ${totalProcessed}개의 레시피 처리")
        return totalProcessed
    }

    private fun convertRecipesToDocument(recipes: Recipes): Document {
        val sb = StringBuilder()

        // 기본 정보
        sb.appendLine("레시피 ID: ${recipes.recipeId}")
        sb.appendLine("제목: ${recipes.title}")

        if (!recipes.description.isNullOrBlank()) {
            sb.appendLine("설명: ${recipes.description}")
        }

        // 재료 정보 파싱 및 추가
        sb.appendLine("재료:")
        try {
            recipes.ingredients.forEach {
                sb.append("  - ${it.name}")
                if (it.amount != null && !it.unit.isNullOrBlank()) {
                    sb.append(" ${it.amount}${it.unit}")
                } else if (it.amount != null) {
                    sb.append(" ${it.amount}")
                } else if (!it.unit.isNullOrBlank()) {
                    sb.append(" ${it.unit}")
                }

                if (!it.notes.isNullOrBlank()) {
                    sb.append(" (${it.notes})")
                }
                sb.appendLine()
            }
        } catch (e: Exception) {
            logger.warn("재료 정보 파싱 실패: ${recipes.ingredients}", e)
            sb.appendLine("  ${recipes.ingredients}")
        }

        // 양념 정보 파싱 및 추가
        sb.appendLine("양념:")
        try {
            recipes.seasonings.forEach {
                sb.append("  - ${it.name}")
                if (it.amount != null && !it.unit.isNullOrBlank()) {
                    sb.append(" ${it.amount}${it.unit}")
                } else if (it.amount != null) {
                    sb.append(" ${it.amount}")
                } else if (!it.unit.isNullOrBlank()) {
                    sb.append(" ${it.unit}")
                }
                sb.appendLine()
            }
        } catch (e: Exception) {
            logger.warn("양념 정보 파싱 실패: ${recipes.seasonings}", e)
            sb.appendLine("  ${recipes.seasonings}")
        }

        // 조리 단계 파싱 및 추가
        sb.appendLine("조리방법:")
        try {
            recipes.instructions
                .sortedBy { it.stepNumber }
                .forEach { (number, description) ->
                    sb.appendLine("  $number. $description")
                }
        } catch (e: Exception) {
            logger.warn("조리 단계 파싱 실패: ${recipes.instructions}", e)
            sb.appendLine("  ${recipes.instructions}")
        }

        // 추가 정보
        recipes.prepTime?.let { sb.appendLine("준비시간: ${it}분") }
        recipes.cookTime?.let { sb.appendLine("조리시간: ${it}분") }
        recipes.servingSize?.let { sb.appendLine("양: ${it}인분") }
        recipes.difficulty?.let { sb.appendLine("난이도: $it") }

        // 태그 정보 - List<String> 타입으로 수정됨
        recipes.tags?.let { tagsList ->
            if (tagsList.isNotEmpty()) {
                sb.append("태그: ")
                sb.appendLine(tagsList.joinToString(", "))
            }
        }

        // 카테고리 정보 추가
        recipes.categoryType?.let { category ->
            sb.appendLine("종류: ${category.name ?: category.categoryId}")
        }

        recipes.categorySituation?.let { category ->
            sb.appendLine("상황: ${category.name ?: category.categoryId}")
        }

        recipes.categoryIngredient?.let { category ->
            sb.appendLine("주재료: ${category.name ?: category.categoryId}")
        }

        recipes.categoryMethod?.let { category ->
            sb.appendLine("조리방법: ${category.name ?: category.categoryId}")
        }

        // 메타데이터 구성
        val metadata = mutableMapOf<String, Any>()
        recipes.recipeId?.let { metadata["recipe_id"] = it.toString() }
        metadata["title"] = recipes.title
        metadata["document_type"] = "recipes"

        recipes.categoryType?.let { metadata["category_type"] = it.categoryId.toString() }
        recipes.categorySituation?.let { metadata["category_situation"] = it.categoryId.toString() }
        recipes.categoryIngredient?.let {
            metadata["category_ingredient"] = it.categoryId.toString()
        }
        recipes.categoryMethod?.let { metadata["category_method"] = it.categoryId.toString() }

        // 태그 메타데이터 추가 - List<String> 타입으로 수정됨
        recipes.tags?.let { tagsList ->
            if (tagsList.isNotEmpty()) {
                metadata["tags"] = tagsList.joinToString(",")
            }
        }

        return Document(sb.toString(), metadata)
    }
}
