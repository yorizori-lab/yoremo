package com.yorizori.yoremo.domain.mealrecords.service

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.CreateMealRecords
import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import com.yorizori.yoremo.domain.mealrecords.port.MealRecordsRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class CreateMealRecordsService(
    private val mealRecordsRepository: MealRecordsRepository
) {
    @Transactional
    fun create(
        request: CreateMealRecords.Request,
        userId: Long
    ): CreateMealRecords.Response {
        val mealRecords = request.mealPlans.map { mealPlan ->
            MealRecords(
                userId = userId,
                mealDate = request.startDate.plus(
                    Duration.ofDays((mealPlan.day - 1).toLong())
                ),
                foodName = mealPlan.foodName,
                mealType = MealRecords.MealType.valueOf(mealPlan.mealType),
                amount = mealPlan.amount,
                unit = mealPlan.unit,
                totalCalories = mealPlan.calories
            )
        }

        mealRecordsRepository.saveAll(mealRecords)

        return CreateMealRecords.Response(success = true)
    }
}
