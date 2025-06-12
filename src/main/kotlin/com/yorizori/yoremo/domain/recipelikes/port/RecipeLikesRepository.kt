package com.yorizori.yoremo.domain.recipelikes.port

import com.yorizori.yoremo.adapter.out.persistence.recipelikes.RecipeLikesJpaRepository
import com.yorizori.yoremo.domain.recipelikes.entity.RecipeLikes
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class RecipeLikesRepository(
    private val recipeLikesJpaRepository: RecipeLikesJpaRepository
) {
    fun findById(id: Long): RecipeLikes? {
        return recipeLikesJpaRepository.findById(id).getOrNull()
    }

    fun findByRecipeId(recipeId: Long): List<RecipeLikes> {
        return recipeLikesJpaRepository.findByRecipeId(recipeId)
    }

    fun findByRecipeIdAndUserId(recipeId: Long, userId: Long): RecipeLikes? {
        return recipeLikesJpaRepository.findByRecipeIdAndUserId(recipeId, userId)
    }

    fun countByRecipeId(recipeId: Long): Long {
        return recipeLikesJpaRepository.countByRecipeId(recipeId)
    }

    fun save(recipeLike: RecipeLikes): RecipeLikes {
        return recipeLikesJpaRepository.save(recipeLike)
    }

    fun delete(recipeLike: RecipeLikes) {
        recipeLikesJpaRepository.delete(recipeLike)
    }
}
