package com.yorizori.yoremo.adapter.out.persistence.recipelikes

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.recipelikes.entity.QRecipeLikes.recipeLikes
import org.springframework.stereotype.Repository

@Repository
class RecipeLikesAdapter(
    private val queryFactory: JPAQueryFactory
) {

    fun countByRecipeIdIn(recipeIds: List<Long>): Map<Long, Long> {
        return queryFactory
            .select(recipeLikes.recipeId, recipeLikes.count())
            .from(recipeLikes)
            .where(recipeLikes.recipeId.`in`(recipeIds))
            .groupBy(recipeLikes.recipeId)
            .fetch()
            .associate { it.get(recipeLikes.recipeId)!! to it.get(recipeLikes.count())!! }
    }
}
