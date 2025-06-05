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

    data class MealPlan(
        val day: Int,
        val mealType: String,
        val foodName: String,
        val amount: Int?,
        val unit: String,
        val calories: Int?
    )
}
