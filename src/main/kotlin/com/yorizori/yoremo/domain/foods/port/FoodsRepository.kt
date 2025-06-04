package com.yorizori.yoremo.domain.foods.port

import com.yorizori.yoremo.adapter.out.persistence.foods.FoodsJpaRepository
import com.yorizori.yoremo.domain.foods.entity.Foods
import org.springframework.stereotype.Component

@Component
class FoodsRepository(
    private val foodsJpaRepository: FoodsJpaRepository
) {
    fun findById(id: Long): Foods? {
        return foodsJpaRepository.findById(id).orElse(null)
    }

    fun save(foods: Foods): Foods {
        return foodsJpaRepository.save(foods)
    }

    fun deleteByRecipeId(recipeId: Long) {
        return foodsJpaRepository.deleteByRecipeId(recipeId)
    }

    fun findByRecipeId(recipeId: Long): Foods {
        return foodsJpaRepository.findByRecipeId(recipeId)
    }
}
