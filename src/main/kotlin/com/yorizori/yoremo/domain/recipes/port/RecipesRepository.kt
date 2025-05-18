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

    fun save(recipes: Recipes): Recipes {
        return recipesJpaRepository.save(recipes)
    }
}
