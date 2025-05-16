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

            logger.info("페이지 ${pageNumber}: ${recipesList.size}개 레시피 처리 중...")

            // 레시피를 문서로 변환
            val documents = recipesList.map { recipes ->
                convertRecipesToDocument(recipes)
            }

            // 문서 분할 - 안전한 호출 추가 및 빈 리스트 대체
            val tokenizedDocuments = tokenTextSplitter.apply(documents)
            val documentsToAdd = tokenizedDocuments ?: emptyList()

            logger.info("페이지 ${pageNumber}: ${documentsToAdd.size}개의 문서로 분할")

            if (documentsToAdd.isNotEmpty()) {
                // 벡터 스토어에 추가
                vectorStore.add(documentsToAdd)
                logger.info("페이지 ${pageNumber}: 벡터 스토어에 문서 추가 완료")
            } else {
                logger.warn("페이지 ${pageNumber}: 분할된 문서가 없습니다")
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
            val ingredientsArray = objectMapper.readTree(recipes.ingredients)
            ingredientsArray.forEach { ingredient ->
                val name = ingredient["name"]?.asText() ?: ""
                val amount = ingredient["amount"]?.asText() ?: ingredient["amount"]?.asDouble()?.toString() ?: ""
                val unit = ingredient["unit"]?.asText() ?: ""
                val notes = ingredient["notes"]?.asText() ?: ""

                sb.append("  - $name")
                if (amount.isNotBlank() && unit.isNotBlank()) {
                    sb.append(" $amount$unit")
                } else if (amount.isNotBlank()) {
                    sb.append(" $amount")
                } else if (unit.isNotBlank()) {
                    sb.append(" $unit")
                }

                if (notes.isNotBlank()) {
                    sb.append(" ($notes)")
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
            val seasoningsArray = objectMapper.readTree(recipes.seasonings)
            seasoningsArray.forEach { seasoning ->
                val name = seasoning["name"]?.asText() ?: ""
                val amount = seasoning["amount"]?.asText() ?: seasoning["amount"]?.asDouble()?.toString() ?: ""
                val unit = seasoning["unit"]?.asText() ?: ""

                sb.append("  - $name")
                if (amount.isNotBlank() && unit.isNotBlank()) {
                    sb.append(" $amount$unit")
                } else if (amount.isNotBlank()) {
                    sb.append(" $amount")
                } else if (unit.isNotBlank()) {
                    sb.append(" $unit")
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
            val instructionsArray = objectMapper.readTree(recipes.instructions)
            // step_number 기준으로 정렬하기 위한 리스트 생성
            val steps = mutableListOf<Pair<Int, String>>()

            instructionsArray.forEach { step ->
                val stepNumber = step["step_number"]?.asInt() ?: 0
                val description = step["description"]?.asText() ?: ""
                steps.add(Pair(stepNumber, description))
            }

            // 단계 번호대로 정렬
            steps.sortBy { it.first }

            // 정렬된 단계 출력
            steps.forEach { (number, description) ->
                sb.appendLine("  ${number}. $description")
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

        // 태그 정보
        recipes.tagsText?.let { tagsText ->
            if (tagsText.isNotBlank()) {
                // 태그 텍스트 파싱 (PostgreSQL의 _text 타입을 문자열로 처리)
                try {
                    // 태그 문자열에서 중괄호와 쌍따옴표 제거, 쉼표로 분리
                    val tagsString = tagsText.trim()
                        .removePrefix("{").removeSuffix("}")
                        .replace("\"", "")

                    val tags = tagsString.split(",").filter { it.isNotBlank() }
                    if (tags.isNotEmpty()) {
                        sb.append("태그: ")
                        sb.appendLine(tags.joinToString(", "))
                    }
                } catch (e: Exception) {
                    logger.warn("태그 파싱 실패: $tagsText", e)
                    sb.appendLine("태그: $tagsText")
                }
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
        recipes.categoryIngredient?.let { metadata["category_ingredient"] = it.categoryId.toString() }
        recipes.categoryMethod?.let { metadata["category_method"] = it.categoryId.toString() }

        recipes.tagsText?.let { tagsText ->
            try {
                val tagsString = tagsText.trim().removePrefix("{").removeSuffix("}").replace("\"", "")
                val tags = tagsString.split(",").filter { it.isNotBlank() }
                if (tags.isNotEmpty()) {
                    metadata["tags"] = tags.joinToString(",")
                }
            } catch (e: Exception) {
                logger.warn("메타데이터 태그 파싱 실패: $tagsText", e)
            }
        }

        return Document(sb.toString(), metadata)
    }
}