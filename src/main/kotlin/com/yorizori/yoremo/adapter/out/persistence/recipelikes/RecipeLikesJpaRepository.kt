package com.yorizori.yoremo.adapter.out.persistence.recipelikes

import com.yorizori.yoremo.domain.recipelikes.entity.RecipeLikes
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeLikesJpaRepository : JpaRepository<RecipeLikes, Long> {

    fun findByRecipeId(recipeId: Long): List<RecipeLikes>

    fun findByRecipeIdAndUserId(recipeId: Long, userId: Long): RecipeLikes?

    fun countByRecipeId(recipeId: Long): Long
}
