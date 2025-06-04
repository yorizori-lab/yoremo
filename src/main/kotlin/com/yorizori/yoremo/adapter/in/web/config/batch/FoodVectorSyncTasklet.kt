package com.yorizori.yoremo.adapter.`in`.web.config.batch

import com.yorizori.yoremo.adapter.out.persistence.foods.FoodsAdapter
import com.yorizori.yoremo.adapter.out.persistence.recipes.RecipesJpaRepository
import com.yorizori.yoremo.domain.foods.entity.Foods
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class FoodVectorSyncTasklet(
    private val foodsAdapter: FoodsAdapter,
    private val recipesJpaRepository: RecipesJpaRepository,
    private val vectorStore: VectorStore
) : Tasklet {

    private val tokenTextSplitter = TokenTextSplitter()

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus? {
        val needSyncFoods = foodsAdapter.findFoodsNeedingVectorSync()

        var successCount = 0
        var failCount = 0

        needSyncFoods.forEach { food ->
            try {
                val recipe = food.recipeId?.let {
                    recipesJpaRepository.findById(it).orElse(null)
                }

                deleteExistingVector(food.foodId!!)

                val document = createDocument(food, recipe)
                val tokenizedDocs = tokenTextSplitter.apply(listOf(document))

                if (!tokenizedDocs.isNullOrEmpty()) {
                    vectorStore.add(tokenizedDocs)
                }

                foodsAdapter.updateVectorSyncedAtOnly(food.foodId!!, Instant.now())

                successCount++
            } catch (_: Exception) {
                failCount++
            }
        }
        return RepeatStatus.FINISHED
    }

    private fun deleteExistingVector(foodId: Long) {
        try {
            vectorStore.delete(listOf("food_id == '$foodId'"))
        } catch (_: Exception) {
        }
    }

    private fun createDocument(food: Foods, recipe: Recipes?): Document {
        val sb = StringBuilder()

        sb.appendLine("식재료 ID: ${food.foodId}")
        sb.appendLine("이름: ${food.name}")
        sb.appendLine("타입: ${food.foodType.description}")
        sb.appendLine("칼로리(100g당): ${food.caloriesPer100g}kcal")

        recipe?.let { r ->
            sb.appendLine("\n=== 관련 레시피 정보 ===")
            sb.appendLine("레시피 ID: ${r.recipeId}")
            sb.appendLine("제목: ${r.title}")

            if (!r.description.isNullOrBlank()) {
                sb.appendLine("설명: ${r.description}")
            }

            sb.appendLine("재료:")
            try {
                r.ingredients.forEach {
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
            } catch (_: Exception) {
                sb.appendLine("  ${r.ingredients}")
            }

            sb.appendLine("양념:")
            try {
                r.seasonings.forEach {
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
            } catch (_: Exception) {
                sb.appendLine("  ${r.seasonings}")
            }

            sb.appendLine("조리방법:")
            try {
                r.instructions
                    .sortedBy { it.stepNumber }
                    .forEach { instruction ->
                        sb.appendLine("  ${instruction.stepNumber}. ${instruction.description}")
                    }
            } catch (_: Exception) {
                sb.appendLine("  ${r.instructions}")
            }

            r.prepTime?.let { sb.appendLine("준비시간: ${it}분") }
            r.cookTime?.let { sb.appendLine("조리시간: ${it}분") }
            r.servingSize?.let { sb.appendLine("양: ${it}인분") }
            r.difficulty?.let { sb.appendLine("난이도: $it") }

            r.tags?.let { tagsList ->
                if (tagsList.isNotEmpty()) {
                    sb.append("태그: ")
                    sb.appendLine(tagsList.joinToString(", "))
                }
            }

            r.categoryType?.let { category ->
                sb.appendLine("종류: ${category.name}")
            }

            r.categorySituation?.let { category ->
                sb.appendLine("상황: ${category.name}")
            }

            r.categoryIngredient?.let { category ->
                sb.appendLine("주재료: ${category.name}")
            }

            r.categoryMethod?.let { category ->
                sb.appendLine("조리방법: ${category.name}")
            }
        }

        val metadata = mutableMapOf<String, Any>()
        metadata["food_id"] = food.foodId.toString()
        metadata["food_name"] = food.name
        metadata["food_type"] = food.foodType.name
        metadata["calories_per100g"] = food.caloriesPer100g.toString()
        metadata["document_type"] = "food"
        metadata["synced_at"] = Instant.now().toString()

        recipe?.let { r ->
            r.recipeId?.let { metadata["recipe_id"] = it.toString() }
            metadata["recipe_title"] = r.title

            r.categoryType?.let { metadata["category_type"] = it.categoryId.toString() }
            r.categorySituation?.let { metadata["category_situation"] = it.categoryId.toString() }
            r.categoryIngredient?.let { metadata["category_ingredient"] = it.categoryId.toString() }
            r.categoryMethod?.let { metadata["category_method"] = it.categoryId.toString() }

            r.tags?.let { tagsList ->
                if (tagsList.isNotEmpty()) {
                    metadata["tags"] = tagsList.joinToString(",")
                }
            }
        }

        return Document(sb.toString(), metadata)
    }
}
