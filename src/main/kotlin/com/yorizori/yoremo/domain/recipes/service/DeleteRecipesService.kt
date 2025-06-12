package com.yorizori.yoremo.domain.recipes.service

import com.yorizori.yoremo.adapter.`in`.web.recipes.message.DeleteRecipes
import com.yorizori.yoremo.domain.common.checkOwnership
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteRecipesService(
    private val recipesRepository: RecipesRepository
) {
    fun delete(id: Long, userId: Long): DeleteRecipes.Response {
        val recipes = recipesRepository.findById(id)
            .checkOwnership(userId)

        recipesRepository.deleteById(id)

        return DeleteRecipes.Response(
            recipeId = recipes.recipeId!!
        )
    }
}
