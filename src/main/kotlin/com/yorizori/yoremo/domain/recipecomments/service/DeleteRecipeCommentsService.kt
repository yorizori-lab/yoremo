package com.yorizori.yoremo.domain.recipecomments.service

import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.DeleteRecipeComments
import com.yorizori.yoremo.domain.common.checkOwnership
import com.yorizori.yoremo.domain.recipecomments.port.RecipeCommentsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteRecipeCommentsService(
    private val recipeCommentsRepository: RecipeCommentsRepository
) {

    @Transactional
    fun delete(commentId: Long, userId: Long): DeleteRecipeComments.Response {
        val comment = recipeCommentsRepository.findById(commentId)
            .checkOwnership(userId)

        if (comment.isDeleted) {
            throw IllegalArgumentException("이미 삭제된 댓글입니다")
        }

        val hasChildComments = recipeCommentsRepository.existsByParentCommentId(commentId)

        if (hasChildComments) {
            val deletedComment = comment.copy(
                isDeleted = true,
                content = "[삭제된 댓글입니다]"
            )
            recipeCommentsRepository.save(deletedComment)

            return DeleteRecipeComments.Response(
                commentId = commentId
            )
        } else {
            recipeCommentsRepository.delete(comment)
            return DeleteRecipeComments.Response(
                commentId = commentId
            )
        }
    }
}
