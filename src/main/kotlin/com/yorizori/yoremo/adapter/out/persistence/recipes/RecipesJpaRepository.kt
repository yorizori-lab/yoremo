package com.yorizori.yoremo.adapter.out.persistence.recipes

import com.yorizori.yoremo.domain.recipes.entity.Recipes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecipesJpaRepository : JpaRepository<Recipes, Long> {

    @Query("""
    SELECT r FROM Recipes r 
    WHERE (:categoryTypeId IS NULL OR r.categoryType.categoryId = :categoryTypeId)
    AND (:categorySituationId IS NULL OR r.categorySituation.categoryId = :categorySituationId)
    AND (:categoryIngredientId IS NULL OR r.categoryIngredient.categoryId = :categoryIngredientId)
    AND (:categoryMethodId IS NULL OR r.categoryMethod.categoryId = :categoryMethodId)
    AND (:#{#tags == null || #tags.isEmpty()} = true OR 
         function('array_overlaps', r.tags, :tags) = true)
    """)
    fun findRecipesByFilters(
        categoryTypeId: Long?,
        categorySituationId: Long?,
        categoryIngredientId: Long?,
        categoryMethodId: Long?,
        difficulty: String?,
        tags: List<String>?
    ): List<Recipes>
}
