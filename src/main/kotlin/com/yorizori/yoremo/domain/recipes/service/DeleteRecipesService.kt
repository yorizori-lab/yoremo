package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.DeleteRecipes
import com.yorizori.yoremo.domain.foods.port.FoodsRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class DeleteRecipesService(
    private val recipesRepository: RecipesRepository,
    private val foodsRepository: FoodsRepository
) {
    fun delete(id: Long): DeleteRecipes.Response {
        val recipes = recipesRepository.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "recipes not found with id: $id"
            )

        val foods = foodsRepository.findByRecipeId(id)

        foodsRepository.deleteById(foods.foodId!!)

        recipesRepository.deleteById(id)

        return DeleteRecipes.Response(
            recipeId = recipes.recipeId!!,
            foodId = foods.foodId!!
        )
    }
}
