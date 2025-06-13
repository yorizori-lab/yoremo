package com.yorizori.yoremo.adapter.out.persistence.recipecomments

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.recipecomments.entity.QRecipeComments.recipeComments
import org.springframework.stereotype.Repository

@Repository
class RecipeCommentsAdapter(
    private val queryFactory: JPAQueryFactory
) {

    fun countByRecipeIdIn(recipeIds: List<Long>): Map<Long, Long> {
        return queryFactory
            .select(recipeComments.recipeId, recipeComments.count())
            .from(recipeComments)
            .where(recipeComments.recipeId.`in`(recipeIds))
            .groupBy(recipeComments.recipeId)
            .fetch()
            .associate { it.get(recipeComments.recipeId)!! to it.get(recipeComments.count())!! }
    }
}
