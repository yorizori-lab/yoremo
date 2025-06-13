package com.yorizori.yoremo.domain.recipecomments.service

import com.yorizori.yoremo.adapter.`in`.web.recipecomments.message.ListRecipeComments
import com.yorizori.yoremo.domain.recipecomments.entity.RecipeComments
import com.yorizori.yoremo.domain.recipecomments.port.RecipeCommentsRepository
import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListRecipeCommentsService(
    private val recipeCommentsRepository: RecipeCommentsRepository,
    private val recipesRepository: RecipesRepository,
    private val usersRepository: UsersRepository
) {

    @Transactional
    fun list(
        recipeId: Long,
        request: ListRecipeComments.Request
    ): ListRecipeComments.Response {
        recipesRepository.findById(recipeId)
            ?: throw IllegalArgumentException("존재하지 않는 레시피입니다: $recipeId")

        val commentsPage = recipeCommentsRepository.findByRecipeIdOrderByCreatedAtAsc(
            recipeId,
            PageRequest.of(
                request.page ?: 0,
                request.pageSize ?: 20,
                Sort.by(Sort.Direction.ASC, "createdAt")
            )
        )

        val totalCount = recipeCommentsRepository.countByRecipeId(recipeId)

        val userIds = commentsPage.content.map { it.userId }.distinct()
        val users = usersRepository.findAllById(userIds)
        val userMap = users.associateBy { it.userId!! }

        val responseItems = buildHierarchicalComments(commentsPage.content, userMap)

        return ListRecipeComments.Response(
            totalCount = totalCount.toInt(),
            comments = responseItems
        )
    }

    private fun buildHierarchicalComments(
        comments: List<RecipeComments>,
        userMap: Map<Long, Users>
    ): List<ListRecipeComments.ResponseItem> {
        val parentComments = comments.filter { it.parentCommentId == null }

        return parentComments.map { parent ->
            buildCommentItem(parent, comments, userMap)
        }
    }

    private fun buildCommentItem(
        comment: RecipeComments,
        allComments: List<RecipeComments>,
        userMap: Map<Long, Users>
    ): ListRecipeComments.ResponseItem {
        val childComments = allComments
            .filter { it.parentCommentId == comment.commentId }
            .map { child -> buildCommentItem(child, allComments, userMap) }

        val userName = userMap[comment.userId]?.name ?: "Unknown User"

        return ListRecipeComments.ResponseItem(
            commentId = comment.commentId!!,
            content = comment.content,
            userId = comment.userId,
            userName = userName,
            parentCommentId = comment.parentCommentId,
            depth = comment.depth,
            isDeleted = comment.isDeleted,
            childComments = childComments,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt
        )
    }
}
