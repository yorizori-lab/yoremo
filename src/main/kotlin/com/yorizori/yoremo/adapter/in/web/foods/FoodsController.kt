package com.yorizori.yoremo.adapter.`in`.web.foods

import com.yorizori.yoremo.adapter.`in`.web.foods.message.Calorie
import com.yorizori.yoremo.adapter.`in`.web.foods.message.RecommendIngredients
import com.yorizori.yoremo.domain.foods.service.CalorieCalculatorService
import com.yorizori.yoremo.domain.foods.service.RecommendIngredientsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/foods/v1")
class FoodsController(
    private val calorieCalculatorService: CalorieCalculatorService,
    private val recommendIngredientsService: RecommendIngredientsService
) {

    @PostMapping("/calculate")
    fun calculateCalories(
        @RequestBody request: Calorie.Request
    ): Calorie.Response {
        return calorieCalculatorService.caculateCalories(request)
    }

    @PostMapping("/recommend")
    fun recommendIngredients(
        @RequestBody request: RecommendIngredients.Request
    ): RecommendIngredients.Response {
        return recommendIngredientsService.recommendIngredients(request)
    }
}
