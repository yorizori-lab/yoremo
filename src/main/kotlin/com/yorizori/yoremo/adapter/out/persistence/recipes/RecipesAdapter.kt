package com.yorizori.yoremo.adapter.out.persistence.recipes

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.categories.entity.QCategories
import com.yorizori.yoremo.domain.recipes.entity.QRecipes.recipes
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import com.yorizori.yoremo.domain.recipes.port.RecipesSearchCommand
import org.springframework.stereotype.Repository

@Repository
class RecipesAdapter(
    private val queryFactory: JPAQueryFactory
) {
    fun search(command: RecipesSearchCommand): List<Recipes> {
        return queryFactory
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
            .fetch()
    }

    /**
     * 태그 검색 조건을 생성합니다 (OR 조건).
     */
    private fun buildTagsCondition(tags: List<String>): BooleanExpression? {
        if (tags.isEmpty()) {
            return null
        }

        var condition: BooleanExpression? = null

        for (tag in tags) {

            val tagWithDelimiter = ",$tag,"

            val tagCondition = Expressions.stringTemplate(
                "concat(',', array_to_string({0}, ','), ',')",
                recipes.tags
            ).like("%$tagWithDelimiter%")

            condition = if (condition == null) {
                tagCondition
            } else {
                condition.or(tagCondition)
            }
        }

        return condition
    }
}
