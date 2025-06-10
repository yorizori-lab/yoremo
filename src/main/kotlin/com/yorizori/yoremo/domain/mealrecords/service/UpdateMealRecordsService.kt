package com.yorizori.yoremo.domain.mealrecords.service

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.UpdateMealRecords
import com.yorizori.yoremo.domain.mealrecords.port.MealRecordsRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UpdateMealRecordsService(
    private val mealRecordsRepository: MealRecordsRepository
) {

    @Transactional
    fun update(request: UpdateMealRecords.Request, userId: Long): UpdateMealRecords.Response {

        val existingRecords = mealRecordsRepository.findAllById(
            request.mealRecords.map { it.recordId }
        )

        val unauthorizedRecords = existingRecords.filter { it.userId != userId }
        if (unauthorizedRecords.isNotEmpty()) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "접근 권한이 없습니다."
            )
        }

        val existingRecordsMap = existingRecords.associateBy { it.recordId }

        val updatedRecords = request.mealRecords.map { updateRecord ->
            val existingRecord = existingRecordsMap[updateRecord.recordId]!!

            existingRecord.copy(
                mealDate = updateRecord.mealDate,
                foodName = updateRecord.foodName,
                mealType = updateRecord.mealType,
                amount = updateRecord.amount,
                unit = updateRecord.unit,
                totalCalories = updateRecord.totalCalories,
                notes = updateRecord.notes
            )
        }

        mealRecordsRepository.saveAll(updatedRecords)

        return UpdateMealRecords.Response(success = true)
    }
}
