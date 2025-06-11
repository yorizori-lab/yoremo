package com.yorizori.yoremo.adapter.`in`.web.mealrecords.message

import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import java.time.Instant

abstract class SearchMealRecords {
    data class Request(
        val startDate: Instant,
        val endDate: Instant
    )

    data class Response(
        val totalCount: Int,
        val mealRecords: List<ResponseItem>
    )

    data class ResponseItem(
        val recordId: Long,
        val mealDate: String,
        val foodName: String?,
        val mealType: MealRecords.MealType,
        val amount: Int?,
        val unit: String?,
        val totalCalories: Int?,
        val notes: String? = null
    )
}
