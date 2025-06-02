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
            .where(*buildConditions(command).toTypedArray())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(recipes.createdAt.desc())
            .fetch()

        val total = queryFactory
            .select(recipes.count())
            .from(recipes)
            .where(*buildConditions(command).toTypedArray())
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    private fun buildConditions(command: RecipesSearchCommand): Collection<BooleanExpression?> {
        return listOf(
            command.categoryTypeId?.let { recipes.categoryType.categoryId.eq(it) },
            command.categorySituationId?.let { recipes.categorySituation.categoryId.eq(it) },
            command.categoryIngredientId?.let { recipes.categoryIngredient.categoryId.eq(it) },
            command.categoryMethodId?.let { recipes.categoryMethod.categoryId.eq(it) },
            command.difficulty?.let { recipes.difficulty.eq(it) },
            command.tags?.let {
                if (it.isEmpty()) {
                    return@let null
                }

                // PostgreSQL 사용자 정의 함수 호출
                return@let Expressions.booleanTemplate(
                    "function('array_contains_any', {0}, {1}) = true",
                    recipes.tags,
                    it.toTypedArray()
                )
            }
        )
    }
}
