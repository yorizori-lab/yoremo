package com.yorizori.yoremo.domain.recipeviewlogs.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.net.InetAddress
import java.time.LocalDateTime

@Entity
@DynamicUpdate
@Table(name = "recipe_view_logs")
data class RecipeViewLogs(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val logId: Long? = null,

    val recipeId: Long,

    val userId: Long? = null,

    val ipAddress: InetAddress,

    val userAgent: String? = null,

    val viewedAt: LocalDateTime
)
