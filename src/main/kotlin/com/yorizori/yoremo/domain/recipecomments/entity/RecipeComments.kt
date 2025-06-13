package com.yorizori.yoremo.domain.recipecomments.entity

import com.yorizori.yoremo.domain.common.Authorizable
import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
@Table(name = "recipe_comments")
class RecipeComments(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId: Long? = null,

    val recipeId: Long,

    val userId: Long,

    val content: String,

    val parentCommentId: Long? = null,

    val depth: Int = 0,

    val isDeleted: Boolean = false

) : BaseEntity(), Authorizable {

    override fun getOwnerId(): Long {
        return this.userId
    }
}
