package com.yorizori.yoremo.domain.recipes.port

import com.yorizori.yoremo.adapter.out.persistence.recipes.RecipesJpaRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class RecipesRepository(
    private val recipesJpaRepository: RecipesJpaRepository
) {
    fun findById(id: Long): Recipes? {
        return recipesJpaRepository.findById(id).getOrNull()
    }

    // 저장
    fun save(recipes: Recipes): Recipes {
        return recipesJpaRepository.save(recipes)
    }

    // 여러 조건으로 검색
    fun findAllByFilters(
        categoryTypeId: Long? = null,
        categorySituationId: Long? = null,
        categoryIngredientId: Long? = null,
        categoryMethodId: Long? = null,
        difficulty: String? = null,
        tags: List<String>? = null
    ): List<Recipes> {
        return recipesJpaRepository.findRecipesByFilters(
            categoryTypeId,
            categorySituationId,
            categoryIngredientId,
            categoryMethodId,
            difficulty,
            tags)
    }
}
