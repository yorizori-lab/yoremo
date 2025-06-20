package com.yorizori.yoremo.domain.recipecomments.port

import com.yorizori.yoremo.adapter.out.persistence.recipecomments.RecipeCommentsAdapter
import com.yorizori.yoremo.adapter.out.persistence.recipecomments.RecipeCommentsJpaRepository
import com.yorizori.yoremo.domain.recipecomments.entity.RecipeComments
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class RecipeCommentsRepository(
    private val recipeCommentsJpaRepository: RecipeCommentsJpaRepository,
    private val recipeCommentsAdapter: RecipeCommentsAdapter
) {
    fun findById(commentId: Long): RecipeComments? {
        return recipeCommentsJpaRepository.findById(commentId).getOrNull()
    }

    fun save(comment: RecipeComments): RecipeComments {
        return recipeCommentsJpaRepository.save(comment)
    }

    fun delete(comment: RecipeComments) {
        recipeCommentsJpaRepository.delete(comment)
    }

    fun countByRecipeIdIn(recipeIds: List<Long>): Map<Long, Long> {
        return recipeCommentsAdapter.countByRecipeIdIn(recipeIds)
    }

    fun existsByParentCommentId(parentCommentId: Long): Boolean {
        return recipeCommentsJpaRepository.existsByParentCommentId(parentCommentId)
    }

    fun findByRecipeIdOrderByCreatedAtAsc(
        recipeId: Long,
        pageable: Pageable
    ): Page<RecipeComments> {
        return recipeCommentsAdapter.findByRecipeIdOrderByCreatedAtAsc(recipeId, pageable)
    }

    fun countByRecipeId(recipeId: Long): Long {
        return recipeCommentsJpaRepository.countByRecipeId(recipeId)
    }
}
