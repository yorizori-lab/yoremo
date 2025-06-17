package com.yorizori.yoremo.domain.recipelikes.service

import com.yorizori.yoremo.adapter.`in`.web.recipelikes.message.ToggleRecipeLikes
import com.yorizori.yoremo.domain.recipelikes.entity.RecipeLikes
import com.yorizori.yoremo.domain.recipelikes.port.RecipeLikesRepository
import org.springframework.stereotype.Service

@Service
class ToggleRecipeLikesService(
    private val recipeLikesRepository: RecipeLikesRepository
) {

    fun toggle(recipeId: Long, userId: Long): ToggleRecipeLikes.Response {
        val existingLike = recipeLikesRepository.findByRecipeIdAndUserId(recipeId, userId)

        return if (existingLike != null) {
            recipeLikesRepository.delete(existingLike)
            ToggleRecipeLikes.Response(
                isLiked = false,
                totalLikes = recipeLikesRepository.findByRecipeId(recipeId).size.toLong()
            )
        } else {
            recipeLikesRepository.save(
                RecipeLikes(
                    recipeId = recipeId,
                    userId = userId
                )
            )
            ToggleRecipeLikes.Response(
                isLiked = true,
                totalLikes = recipeLikesRepository.findByRecipeId(recipeId).size.toLong()
            )
        }
    }
}
