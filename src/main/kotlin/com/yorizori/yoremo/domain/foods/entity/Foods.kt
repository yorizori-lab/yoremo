package com.yorizori.yoremo.domain.foods.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "foods")
data class Foods (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val foodId: Long? = null,

    val name: String,

    @Enumerated(EnumType.STRING)
    val foodType: FoodType = FoodType.BASIC,

    val recipeId: Long? = null,

    val caloriesPer100g: Long? = null
) : BaseEntity() {
    enum class FoodType(val description: String) {
        BASIC("기본"),
        RECIPE("레시피"),
    }
}
