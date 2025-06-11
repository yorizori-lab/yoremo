package com.yorizori.yoremo.domain.mealrecords.service

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.SearchMealRecords
import com.yorizori.yoremo.domain.mealrecords.port.MealRecordsRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SearchMealRecordsService(
    private val mealRecordsRepository: MealRecordsRepository
) {

    @Transactional
    fun search(request: SearchMealRecords.Request, userId: Long): SearchMealRecords.Response {
        val mealRecords = mealRecordsRepository.findByUserIdAndMealDateBetweenOrderByMealDateAsc(
            userId = userId,
            startDate = request.startDate,
            endDate = request.endDate
        )

        val responseItems = mealRecords.map { record ->
            SearchMealRecords.ResponseItem(
                recordId = record.recordId!!,
                mealDate = record.mealDate.toString(),
                foodName = record.foodName,
                mealType = record.mealType,
                amount = record.amount,
                unit = record.unit,
                totalCalories = record.totalCalories,
                notes = record.notes
            )
        }

        return SearchMealRecords.Response(
            totalCount = responseItems.size,
            mealRecords = responseItems
        )
    }
}
