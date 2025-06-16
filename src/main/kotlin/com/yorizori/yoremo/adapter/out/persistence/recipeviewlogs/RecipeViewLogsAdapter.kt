package com.yorizori.yoremo.adapter.out.persistence.recipeviewlogs

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.categories.entity.QCategories
import com.yorizori.yoremo.domain.foods.entity.QFoods.foods
import com.yorizori.yoremo.domain.recipes.entity.QRecipes.recipes
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipeviewlogs.entity.QRecipeViewLogs.recipeViewLogs
import com.yorizori.yoremo.domain.recipeviewlogs.entity.RecipeViewLogs
import org.springframework.stereotype.Repository
import java.net.InetAddress
import java.time.LocalDateTime

@Repository
class RecipeViewLogsAdapter(
    private val queryFactory: JPAQueryFactory
) {

    fun findRecipeWithRecentViewCheck(
        recipeId: Long,
        ipAddress: InetAddress
    ): Pair<Recipes, RecipeViewLogs?>? {
        val result = queryFactory
            .select(recipes, recipeViewLogs)
            .from(recipes)
            .leftJoin(recipes.categoryType, QCategories(Recipes::categoryType.name)).fetchJoin()
            .leftJoin(
                recipes.categorySituation,
                QCategories(Recipes::categorySituation.name)
            ).fetchJoin()
            .leftJoin(
                recipes.categoryIngredient,
                QCategories(Recipes::categoryIngredient.name)
            ).fetchJoin()
            .leftJoin(recipes.categoryMethod, QCategories(Recipes::categoryMethod.name)).fetchJoin()
            .leftJoin(recipes.food, foods).fetchJoin()
            .leftJoin(recipeViewLogs).on(
                recipes.recipeId.eq(recipeViewLogs.recipeId)
                    .and(recipeViewLogs.ipAddress.eq(ipAddress))
                    .and(recipeViewLogs.viewedAt.after(LocalDateTime.now().minusMinutes(1)))
            )
            .where(recipes.recipeId.eq(recipeId))
            .fetchOne()

        return result?.let {
            val recipe = it.get(recipes)!!
            val viewLog = it.get(recipeViewLogs)
            Pair(recipe, viewLog)
        }
    }

    fun countLogsByRecipeId(recipeId: Long): Long {
        return queryFactory
            .select(recipeViewLogs.count())
            .from(recipeViewLogs)
            .where(recipeViewLogs.recipeId.eq(recipeId))
            .fetchOne() ?: 0L
    }

    fun getViewCountsBetweenDates(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Map<Long, Long> {
        return queryFactory
            .select(
                recipeViewLogs.recipeId,
                recipeViewLogs.recipeId.count()
            )
            .from(recipeViewLogs)
            .where(
                recipeViewLogs.viewedAt.between(startDate, endDate)
            )
            .groupBy(recipeViewLogs.recipeId)
            .fetch()
            .associate { tuple ->
                tuple.get(recipeViewLogs.recipeId)!! to tuple.get(recipeViewLogs.recipeId.count())!!
            }
    }

    fun deleteLogsBeforeDate(cutoffDate: LocalDateTime): Long {
        return queryFactory
            .delete(recipeViewLogs)
            .where(recipeViewLogs.viewedAt.before(cutoffDate))
            .execute()
    }
}
