package com.yorizori.yoremo.adapter.`in`.web.mealrecords.message

abstract class MealPlanRecommendation {
    data class Request(
        val question: String,
        val sessionId: String? = null
    )

    data class Response(
        val answer: String,
        val mealRecords: List<MealPlan>? = null
    )
}
