package com.yorizori.yoremo.adapter.`in`.web.mealrecords.message

import java.time.Instant

abstract class CreateMealRecords {
    data class Request(
        val mealPlans: List<MealPlan>,
        val startDate: Instant = Instant.now()
    )

    data class Response(
        val success: Boolean
    )
}
