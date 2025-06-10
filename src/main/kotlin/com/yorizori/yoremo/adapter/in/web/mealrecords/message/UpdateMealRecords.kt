package com.yorizori.yoremo.adapter.`in`.web.mealrecords.message

import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import java.time.Instant

abstract class UpdateMealRecords {
    data class Request(
        val mealRecords: List<UpdateMealRecord>
    )

    data class Response(
        val success: Boolean
    )

    data class UpdateMealRecord(
        val recordId: Long,
        val mealDate: Instant,
        val foodName: String?,
        val mealType: MealRecords.MealType,
        val amount: Int?,
        val unit: String?,
        val totalCalories: Int?,
        val notes: String? = null
    )
}
