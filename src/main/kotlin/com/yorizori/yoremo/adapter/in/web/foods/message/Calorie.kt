package com.yorizori.yoremo.adapter.`in`.web.foods.message

abstract class Calorie {
    data class Request(
        val title: String,
        val ingredients: List<String>
    )

    data class Response(
        val caloriesPer100g: Int
    )
}
