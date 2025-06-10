package com.yorizori.yoremo.domain.mealrecords.service

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.DeleteMealRecords
import com.yorizori.yoremo.domain.mealrecords.port.MealRecordsRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DeleteMealRecordsService(
    private val mealRecordsRepository: MealRecordsRepository
) {

    @Transactional
    fun delete(request: DeleteMealRecords.Request, userId: Long): DeleteMealRecords.Response {

        val existingRecords = mealRecordsRepository.findAllById(request.recordIds)

        val unauthorizedRecords = existingRecords.filter { it.userId != userId }
        if (unauthorizedRecords.isNotEmpty()) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "접근 권한이 없습니다."
            )
        }

        mealRecordsRepository.deleteAllById(request.recordIds)

        return DeleteMealRecords.Response(success = true)
    }
}
