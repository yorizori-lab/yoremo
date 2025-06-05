package com.yorizori.yoremo.adapter.`in`.web.mealrecords.message

data class MealPlan(
    val day: Int,
    val mealType: String,
    val foodName: String,
    val amount: Int?,
    val unit: String,
    val calories: Int?,
    val notes: String? = null
)
