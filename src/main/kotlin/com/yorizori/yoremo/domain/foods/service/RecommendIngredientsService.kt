package com.yorizori.yoremo.domain.foods.service

import com.yorizori.yoremo.adapter.`in`.web.foods.message.RecommendIngredients
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.filter.Filter
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder
import org.springframework.stereotype.Service

@Service
class RecommendIngredientsService(
    private val vectorStore: VectorStore,
    private val recipesRepository: RecipesRepository
) {

    fun recommendIngredients(request: RecommendIngredients.Request): RecommendIngredients.Response {
        val queryParts = mutableListOf<String>()
        queryParts.addAll(request.ingredients)
        request.categoryType?.let { queryParts.add(it) }
        request.categoryMethod?.let { queryParts.add(it) }
        request.categorySituation?.let { queryParts.add(it) }
        request.categoryIngredient?.let { queryParts.add(it) }
        val query = queryParts.joinToString(" ")

        val filterBuilder = FilterExpressionBuilder()
        val filterExpressions = mutableListOf<Filter.Expression>()

        filterExpressions.add(filterBuilder.ne("recipe_id", "null").build())

        if (request.ingredients.isNotEmpty()) {
            val ingredientFilters = request.ingredients.map { ingredient ->
                filterBuilder.`in`("ingredients", ingredient).build()
            }
            filterExpressions.addAll(ingredientFilters)
        }

        request.categoryType?.let { categoryType ->
            filterExpressions.add(
                filterBuilder.eq("category_type_name", categoryType).build()
            )
        }

        request.categoryMethod?.let { categoryMethod ->
            filterExpressions.add(
                filterBuilder.eq("category_method_name", categoryMethod).build()
            )
        }

        request.categorySituation?.let { categorySituation ->
            filterExpressions.add(
                filterBuilder.eq("category_situation_name", categorySituation).build()
            )
        }

        request.categoryIngredient?.let { categoryIngredient ->
            filterExpressions.add(
                filterBuilder.eq("category_ingredient_name", categoryIngredient).build()
            )
        }

        val finalFilter = if (filterExpressions.size == 1) {
            filterExpressions[0]
        } else {
            var combinedFilter = filterExpressions[0]
            for (i in 1 until filterExpressions.size) {
                combinedFilter = filterBuilder.and(
                    FilterExpressionBuilder.Op(combinedFilter),
                    FilterExpressionBuilder.Op(filterExpressions[i])
                ).build()
            }
            combinedFilter
        }

        val searchResults = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(5)
                .filterExpression(finalFilter)
                .build()
        ) ?: emptyList()

        val recipeIds = searchResults.take(4).mapNotNull { document ->
            document.metadata["recipe_id"]?.toString()?.toLongOrNull()
        }.distinct()

        if (recipeIds.isEmpty()) {
            return RecommendIngredients.Response(emptyList())
        }

        val recipes = recipesRepository.findAllById(recipeIds)

        val recommendations = recipes.map { recipe ->
            RecommendIngredients.RecommendationItem(
                recipeId = recipe.recipeId!!,
                title = recipe.title,
                description = recipe.description,
                imageUrl = recipe.imageUrl,
                ingredients = recipe.ingredients,
                seasonings = recipe.seasonings,
                prepTime = recipe.prepTime,
                cookTime = recipe.cookTime,
                servingSize = recipe.servingSize,
                difficulty = recipe.difficulty?.description,
                tags = recipe.tags,
                caloriesPer100g = recipe.food?.caloriesPer100g
            )
        }

        return RecommendIngredients.Response(recommendations)
    }
}
