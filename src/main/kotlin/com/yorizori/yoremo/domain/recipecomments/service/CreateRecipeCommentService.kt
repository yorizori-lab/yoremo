package com.yorizori.yoremo.domain.recipecomments.service

import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.CreateRecipeComment
import com.yorizori.yoremo.domain.recipecomments.entity.RecipeComments
import com.yorizori.yoremo.domain.recipecomments.port.RecipeCommentsRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import org.springframework.stereotype.Service

@Service
class CreateRecipeCommentService(
    private val recipeCommentsRepository: RecipeCommentsRepository,
    private val recipesRepository: RecipesRepository
) {

    fun create(
        recipeId: Long,
        request: CreateRecipeComment.Request,
        userId: Long
    ): CreateRecipeComment.Response {
        recipesRepository.findById(recipeId)
            ?: throw IllegalArgumentException("존재하지 않는 레시피입니다: $recipeId")

        val depth = if (request.parentCommentId != null) {
            val parent = recipeCommentsRepository.findById(request.parentCommentId)
                ?: throw IllegalArgumentException("부모 댓글을 찾을 수 없습니다")

            if (parent.recipeId != recipeId) {
                throw IllegalArgumentException("다른 레시피의 댓글에는 답글을 달 수 없습니다")
            }

            val newDepth = parent.depth + 1

            if (newDepth > 5) {
                throw IllegalArgumentException("댓글 깊이가 너무 깊습니다. 최대 5단계까지 가능합니다")
            }

            newDepth
        } else {
            0
        }

        // 댓글 생성
        val newComment = RecipeComments(
            recipeId = recipeId,
            userId = userId,
            content = request.content,
            parentCommentId = request.parentCommentId,
            depth = depth,
            isDeleted = false
        )

        val savedComment = recipeCommentsRepository.save(newComment)

        return CreateRecipeComment.Response(
            commentId = savedComment.commentId!!,
            content = savedComment.content,
            depth = savedComment.depth,
            createdAt = savedComment.createdAt,
            updatedAt = savedComment.updatedAt
        )
    }
}
