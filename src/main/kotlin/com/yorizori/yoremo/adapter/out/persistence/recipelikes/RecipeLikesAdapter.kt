package com.yorizori.yoremo.adapter.out.persistence.recipelikes

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.recipelikes.entity.QRecipeLikes.recipeLikes
import com.yorizori.yoremo.domain.recipelikes.entity.RecipeLikes
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class RecipeLikesAdapter(
    private val queryFactory: JPAQueryFactory
) {

    fun findByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<RecipeLikes> {
        val content = queryFactory
            .selectFrom(recipeLikes)
            .where(recipeLikes.userId.eq(userId))
            .orderBy(recipeLikes.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(recipeLikes.count())
            .from(recipeLikes)
            .where(recipeLikes.userId.eq(userId))
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

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
