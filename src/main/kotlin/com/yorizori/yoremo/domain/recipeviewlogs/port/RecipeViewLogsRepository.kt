package com.yorizori.yoremo.domain.recipeviewlogs.port

import com.yorizori.yoremo.adapter.out.persistence.recipeviewlogs.RecipeViewLogsAdapter
import com.yorizori.yoremo.adapter.out.persistence.recipeviewlogs.RecipeViewLogsJpaRepository
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipeviewlogs.entity.RecipeViewLogs
import org.springframework.stereotype.Repository
import java.net.InetAddress
import java.time.LocalDateTime

@Repository
class RecipeViewLogsRepository(
    private val recipeViewLogsJpaRepository: RecipeViewLogsJpaRepository,
    private val recipeViewLogsAdapter: RecipeViewLogsAdapter
) {

    fun save(recipeViewLogs: RecipeViewLogs): RecipeViewLogs {
        return recipeViewLogsJpaRepository.save(recipeViewLogs)
    }

    fun findRecipeWithRecentViewCheck(
        recipeId: Long,
        ipAddress: InetAddress
    ): Pair<Recipes, RecipeViewLogs?>? {
        return recipeViewLogsAdapter.findRecipeWithRecentViewCheck(recipeId, ipAddress)
    }

    fun countLogsByRecipeId(recipeId: Long): Long {
        return recipeViewLogsAdapter.countLogsByRecipeId(recipeId)
    }

    fun getViewCountsBetweenDates(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Map<Long, Long> {
        return recipeViewLogsAdapter.getViewCountsBetweenDates(startDate, endDate)
    }
}
