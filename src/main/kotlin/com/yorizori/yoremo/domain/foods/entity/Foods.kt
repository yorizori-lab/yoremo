package com.yorizori.yoremo.domain.foods.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import com.yorizori.yoremo.domain.recipes.entity.Recipes
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.Instant

@Entity
@DynamicUpdate
@Table(name = "foods")
data class Foods(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val foodId: Long? = null,

    val name: String,

    @Enumerated(EnumType.STRING)
    val foodType: FoodType = FoodType.BASIC,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "recipe_id")
    val recipe: Recipes? = null,

    val caloriesPer100g: Long? = null,

    val vectorSyncedAt: Instant? = null
) : BaseEntity() {
    enum class FoodType(val description: String) {
        BASIC("기본"),
        RECIPE("레시피")
    }
}
