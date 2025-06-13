package com.yorizori.yoremo.adapter.out.persistence.recipecomments

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.recipecomments.entity.QRecipeComments.recipeComments
import com.yorizori.yoremo.domain.recipecomments.entity.RecipeComments
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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

    fun findByRecipeIdOrderByCreatedAtAsc(
        recipeId: Long,
        pageable: Pageable
    ): Page<RecipeComments> {
        val content = queryFactory
            .selectFrom(recipeComments)
            .where(recipeComments.recipeId.eq(recipeId))
            .orderBy(recipeComments.createdAt.asc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(recipeComments.count())
            .from(recipeComments)
            .where(recipeComments.recipeId.eq(recipeId))
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}
