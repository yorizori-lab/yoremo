package com.yorizori.yoremo.adapter.`in`.web.mealrecords

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlanRecommendation
import com.yorizori.yoremo.domain.mealrecords.service.MealPlanRecommendationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/meal-records/v1")
class MealRecordsController(
    private val mealPlanRecommendationService: MealPlanRecommendationService
) {

    @PostMapping("/recommend")
    fun processMessage(
        @RequestBody request: MealPlanRecommendation.Request
    ): MealPlanRecommendation.Response {
        return mealPlanRecommendationService.getResponse(request)
    }
}
