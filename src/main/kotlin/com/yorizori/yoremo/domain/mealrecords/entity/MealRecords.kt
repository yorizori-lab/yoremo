package com.yorizori.yoremo.domain.mealrecords.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "meal_records")
data class MealRecords(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recordId: Long? = null,

    val userId: Long,

    val mealDate: Instant,

    val foodName: String?,

    @Enumerated(EnumType.STRING)
    val mealType: MealType,

    val amount: Int?,

    val unit: String?,

    val totalCalories: Int?,

    val notes: String? = null
) : BaseEntity() {

    enum class MealType(val description: String) {
        BREAKFAST("아침"),
        LUNCH("점심"),
        DINNER("저녁"),
        SNACK("간식")
    }
}
