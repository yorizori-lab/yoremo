package com.yorizori.yoremo.domain.recipes.entity

import com.yorizori.yoremo.domain.categories.entity.Categories
import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "recipes")
data class Recipes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recipeId: Long? = null,

    val title: String,

    val description: String? = null,

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    val ingredients: List<Ingredient>,

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    val seasonings: List<Seasoning>,

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    val instructions: List<Instruction>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_type")
    val categoryType: Categories? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_situation")
    val categorySituation: Categories? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_ingredient")
    val categoryIngredient: Categories? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_method")
    val categoryMethod: Categories? = null,

    val prepTime: Int? = null,

    val cookTime: Int? = null,

    val servingSize: Int? = null,

    val difficulty: String? = null,

    val imageUrl: String? = null,

    @Column(name = "tags", columnDefinition = "_text")
    @JdbcTypeCode(SqlTypes.ARRAY)
    var tags: List<String>? = null
) : BaseEntity() {

    data class Ingredient(
        val name: String,
        val amount: Int?,
        val unit: String?,
        val notes: String?
    )

    data class Seasoning(
        val name: String,
        val amount: Int?,
        val unit: String?
    )

    data class Instruction(
        val stepNumber: Int,
        val description: String,
        val imageUrl: String?
    )
}
