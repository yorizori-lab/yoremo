package com.yorizori.yoremo.domain.recipes.entity

import com.yorizori.yoremo.domain.categories.entity.Categories
import com.yorizori.yoremo.domain.common.Authorizable
import com.yorizori.yoremo.domain.common.BaseEntity
import com.yorizori.yoremo.domain.foods.entity.Foods
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@DynamicUpdate
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

    @Enumerated(EnumType.STRING)
    val difficulty: Difficulty? = null,

    val imageUrl: String? = null,

    @Column(name = "tags", columnDefinition = "_text")
    @JdbcTypeCode(SqlTypes.ARRAY)
    var tags: List<String>? = null,

    @OneToOne(
        mappedBy = "recipe",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    val food: Foods? = null,

    val userId: Long
) : BaseEntity(), Authorizable {

    override fun getOwnerId(): Long {
        return this.userId
    }

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

    enum class Difficulty(val description: String) {
        EASY("쉬움"),
        NORMAL("보통"),
        HARD("어려움")
    }
}
