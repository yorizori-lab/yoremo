package com.yorizori.yoremo.domain.recipelikes.service

import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.CountRecipeLikes
import com.yorizori.yoremo.domain.recipelikes.port.RecipeLikesRepository
import org.springframework.stereotype.Service

@Service
class CountRecipeLikesService(
    private val recipeLikesRepository: RecipeLikesRepository
) {

    fun count(recipeId: Long): CountRecipeLikes.Response {
        return CountRecipeLikes.Response(
            totalLikes = recipeLikesRepository.countByRecipeId(recipeId)
        )
    }
}
