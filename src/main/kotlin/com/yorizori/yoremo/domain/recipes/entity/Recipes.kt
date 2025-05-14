package com.yorizori.yoremo.domain.recipes.entity

import com.yorizori.yoremo.domain.categories.entity.Categories
import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "recipes")
class Recipes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recipeId: Int? = null,

    val title: String,

    val description: String? = null,

    @Column(columnDefinition = "jsonb")
    val ingredients: String,

    @Column(columnDefinition = "jsonb")
    val seasonings: String,

    @Column(columnDefinition = "jsonb")
    val instructions: String,

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
    var tagsText: String? = null,
) : BaseEntity()