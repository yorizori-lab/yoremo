package com.yorizori.yoremo.adapter.`in`.web.config.batch.foodvector

import com.yorizori.yoremo.adapter.out.persistence.foods.FoodsAdapter
import com.yorizori.yoremo.domain.foods.entity.Foods
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.slf4j.LoggerFactory
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
    private val vectorStore: VectorStore
) : Tasklet {

    private val logger = LoggerFactory.getLogger(this::class.java)

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
                try {
                    vectorStore.delete("food_id == '${food.foodId!!}'")
                } catch (e: Exception) {
                    logger.warn("Failed to delete existing vector for food ID: ${food.foodId}", e)
                }

                val document = createDocument(food, food.recipe)
                val tokenizedDocs = tokenTextSplitter.apply(listOf(document))

                if (!tokenizedDocs.isNullOrEmpty()) {
                    vectorStore.add(tokenizedDocs)
                }

                foodsAdapter.updateVectorSyncedAtOnly(food.foodId!!, Instant.now())

                successCount++
            } catch (e: Exception) {
                logger.error("Failed to sync vector for food ID: ${food.foodId}", e)
                failCount++
            }
        }

        logger.info("Food vector sync completed - Success: $successCount, Failed: $failCount")
        return RepeatStatus.FINISHED
    }

    private fun createDocument(food: Foods, recipe: Recipes?): Document {
        val sb = StringBuilder()

        sb.append(food.name)

        recipe?.let { r ->
            sb.append("로 만들 수 있는 ${r.title}")

            try {
                val allIngredients = mutableListOf<String>()

                val ingredientNames = r.ingredients.map { it.name }
                allIngredients.addAll(ingredientNames)

                val seasoningNames = r.seasonings.map { it.name }
                allIngredients.addAll(seasoningNames)

                if (allIngredients.isNotEmpty()) {
                    sb.append(". 필요한 재료: ${allIngredients.joinToString(", ")}")
                }
            } catch (e: Exception) {
                logger.warn("Failed to parse ingredients for food ${food.foodId}", e)
            }

            val characteristics = mutableListOf<String>()

            r.categoryType?.let { characteristics.add(it.name) }
            r.categoryMethod?.let { characteristics.add(it.name) }

            val totalTime = (r.prepTime ?: 0) + (r.cookTime ?: 0)
            if (totalTime > 0) {
                characteristics.add("${totalTime}분")
            }

            r.difficulty?.let { characteristics.add(it.name) }

            if (characteristics.isNotEmpty()) {
                sb.append(". ${characteristics.joinToString(", ")}")
            }

            r.tags?.let { tagsList ->
                if (tagsList.isNotEmpty()) {
                    sb.append(". ${tagsList.joinToString(", ")}")
                }
            }
        } ?: run {
            sb.append("는 다양한 요리에 활용할 수 있는 식재료입니다")
        }

        val metadata = mutableMapOf<String, Any>()

        metadata["food_id"] = food.foodId.toString()
        metadata["recipe_id"] = recipe?.recipeId?.toString() ?: "null"

        metadata["food_name"] = food.name

        recipe?.let { r ->
            metadata["recipe_title"] = r.title

            r.categoryType?.let { metadata["category_type_name"] = it.name }
            r.categoryMethod?.let { metadata["category_method_name"] = it.name }
            r.categorySituation?.let { metadata["category_situation_name"] = it.name }
            r.categoryIngredient?.let { metadata["category_ingredient_name"] = it.name }

            r.prepTime?.let { metadata["prep_time"] = it.toString() }
            r.cookTime?.let { metadata["cook_time"] = it.toString() }
            r.difficulty?.let { metadata["difficulty"] = it.name }

            try {
                val ingredientNames = r.ingredients.map { it.name }
                if (ingredientNames.isNotEmpty()) {
                    metadata["ingredients"] = ingredientNames.toTypedArray()
                }

                val seasoningNames = r.seasonings.map { it.name }
                if (seasoningNames.isNotEmpty()) {
                    metadata["seasonings"] = seasoningNames.toTypedArray()
                }
            } catch (e: Exception) {
                logger.warn("Failed to extract ingredient metadata for food ${food.foodId}", e)
            }

            r.tags?.let { tagsList ->
                if (tagsList.isNotEmpty()) {
                    metadata["tags"] = tagsList.toTypedArray()
                }
            }
        }

        return Document(sb.toString(), metadata)
    }
}
