package com.yorizori.yoremo.domain.recipecomments.service

import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.UpdateRecipeComments
import com.yorizori.yoremo.domain.common.checkOwnership
import com.yorizori.yoremo.domain.recipecomments.port.RecipeCommentsRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UpdateRecipeCommentsService(
    private val recipeCommentsRepository: RecipeCommentsRepository
) {

    @Transactional
    fun update(
        commentId: Long,
        request: UpdateRecipeComments.Request,
        userId: Long
    ): UpdateRecipeComments.Response {
        val comment = recipeCommentsRepository.findById(commentId)
            .checkOwnership(userId)

        if (comment.isDeleted) {
            throw IllegalArgumentException("이미 삭제된 댓글입니다")
        }

        val updatedComment = comment.copy(
            content = request.content
        )

        val savedComment = recipeCommentsRepository.save(updatedComment)

        return UpdateRecipeComments.Response(
            commentId = savedComment.commentId!!
        )
    }
}
