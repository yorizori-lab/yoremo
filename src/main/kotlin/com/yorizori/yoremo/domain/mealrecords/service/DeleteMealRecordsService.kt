package com.yorizori.yoremo.domain.mealrecords.service

import com.yorizori.yoremo.adapter.`in`.web.mealrecords.message.DeleteMealRecords
import com.yorizori.yoremo.domain.common.checkAllOwnership
import com.yorizori.yoremo.domain.mealrecords.port.MealRecordsRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DeleteMealRecordsService(
    private val mealRecordsRepository: MealRecordsRepository
) {

    @Transactional
    fun delete(request: DeleteMealRecords.Request, userId: Long): DeleteMealRecords.Response {
        mealRecordsRepository.findAllById(request.recordIds)
            .checkAllOwnership(userId)

        mealRecordsRepository.deleteAllById(request.recordIds)

        return DeleteMealRecords.Response(success = true)
    }
}
