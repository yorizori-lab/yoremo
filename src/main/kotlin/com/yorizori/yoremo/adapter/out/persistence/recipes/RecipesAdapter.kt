package com.yorizori.yoremo.adapter.out.persistence.recipes

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.categories.entity.QCategories
import com.yorizori.yoremo.domain.recipes.entity.QRecipes.recipes
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesSearchCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class RecipesAdapter(
    private val queryFactory: JPAQueryFactory
) {
    fun search(command: RecipesSearchCommand, pageable: Pageable): Page<Recipes> {
        val content = queryFactory
            .selectFrom(recipes)
            .join(recipes.categoryType, QCategories(Recipes::categoryType.name)).fetchJoin()
            .join(
                recipes.categorySituation,
                QCategories(Recipes::categorySituation.name)
            ).fetchJoin()
            .join(
                recipes.categoryIngredient,
                QCategories(Recipes::categoryIngredient.name)
            ).fetchJoin()
            .join(recipes.categoryMethod, QCategories(Recipes::categoryMethod.name)).fetchJoin()
            .where(
                command.categoryTypeId?.let { recipes.categoryType.categoryId.eq(it) },
                command.categorySituationId?.let { recipes.categorySituation.categoryId.eq(it) },
                command.categoryIngredientId?.let { recipes.categoryIngredient.categoryId.eq(it) },
                command.categoryMethodId?.let { recipes.categoryMethod.categoryId.eq(it) },
                command.difficulty?.let { recipes.difficulty.eq(it) },
                command.tags?.let { buildTagsCondition(it) }
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(recipes.createdAt.desc())
            .fetch()

        val total = queryFactory
            .select(recipes.count())
            .from(recipes)
            .where(
                command.categoryTypeId?.let { recipes.categoryType.categoryId.eq(it) },
                command.categorySituationId?.let { recipes.categorySituation.categoryId.eq(it) },
                command.categoryIngredientId?.let { recipes.categoryIngredient.categoryId.eq(it) },
                command.categoryMethodId?.let { recipes.categoryMethod.categoryId.eq(it) },
                command.difficulty?.let { recipes.difficulty.eq(it) },
                command.tags?.let { buildTagsCondition(it) }
            )
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    private fun buildTagsCondition(tags: List<String>): BooleanExpression? {
        if (tags.isEmpty()) {
            return null
        }

        // PostgreSQL 사용자 정의 함수 호출
        return Expressions.booleanTemplate(
            "function('array_contains_any', {0}, {1}) = true",
            recipes.tags,
            tags.toTypedArray()
        )
    }
}
