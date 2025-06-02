package com.yorizori.yoremo.domain.recipes.port

import com.yorizori.yoremo.adapter.out.persistence.recipes.RecipesAdapter
import com.yorizori.yoremo.adapter.out.persistence.recipes.RecipesJpaRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class RecipesRepository(
    private val recipesJpaRepository: RecipesJpaRepository,
    private val recipesAdapter: RecipesAdapter
) {
    fun findById(id: Long): Recipes? {
        return recipesJpaRepository.findById(id).getOrNull()
    }

    // 저장
    fun save(recipes: Recipes): Recipes {
        return recipesJpaRepository.save(recipes)
    }

    fun deleteById(id: Long) {
        return recipesJpaRepository.deleteById(id)
    }

    // 여러 조건으로 검색
    fun search(
        command: RecipesSearchCommand,
        pageable: Pageable
    ): Page<Recipes> {
        return recipesAdapter.search(command, pageable)
    }
}
