package com.yorizori.yoremo.domain.recipelikes.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
@Table(name = "recipe_likes")
data class RecipeLikes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val likeId: Long? = null,

    val recipeId: Long,

    val userId: Long
) : BaseEntity()
