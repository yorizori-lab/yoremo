package com.yorizori.yoremo.adapter.`in`.web.mealrecords

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.CreateMealRecords
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.DeleteMealRecords
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.MealPlanRecommendation
import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.UpdateMealRecords
import com.yorizori.yoremo.domain.mealrecords.service.CreateMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.DeleteMealRecordsService
import com.yorizori.yoremo.domain.mealrecords.service.MealPlanRecommendationService
import com.yorizori.yoremo.domain.mealrecords.service.UpdateMealRecordsService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/meal-records/v1")
class MealRecordsController(
    private val mealPlanRecommendationService: MealPlanRecommendationService,
    private val createMealRecordsService: CreateMealRecordsService,
    private val updateMealRecordsService: UpdateMealRecordsService,
    private val deleteMealRecordsService: DeleteMealRecordsService
) {

    @PostMapping("/recommend")
    fun processMessage(
        @RequestBody request: MealPlanRecommendation.Request
    ): MealPlanRecommendation.Response {
        return mealPlanRecommendationService.getResponse(request)
    }

    @PostMapping("/meal-records")
    fun createMealRecords(
        @RequestBody request: CreateMealRecords.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): CreateMealRecords.Response {
        return createMealRecordsService.create(request, authentication.userId)
    }

    @PutMapping("/meal-records")
    fun updateMealRecords(
        @RequestBody request: UpdateMealRecords.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): UpdateMealRecords.Response {
        return updateMealRecordsService.update(request, authentication.userId)
    }

    @DeleteMapping("/meal-records")
    fun deleteMealRecords(
        @RequestBody request: DeleteMealRecords.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): DeleteMealRecords.Response {
        return deleteMealRecordsService.delete(request, authentication.userId)
    }
}
