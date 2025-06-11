package com.yorizori.yoremo.domain.mealrecords.port

import com.yorizori.yoremo.adapter.out.persistence.mealrecords.MealRecordsJpaRepository
import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class MealRecordsRepository(
    private val mealRecordsJpaRepository: MealRecordsJpaRepository
) {

    fun findById(id: Long): MealRecords? {
        return mealRecordsJpaRepository.findById(id).getOrNull()
    }

    fun findAllById(ids: List<Long>): List<MealRecords> {
        return mealRecordsJpaRepository.findAllById(ids)
    }

    fun saveAll(mealRecords: List<MealRecords>): List<MealRecords> {
        return mealRecordsJpaRepository.saveAll(mealRecords)
    }

    fun deleteAllById(ids: List<Long>) {
        mealRecordsJpaRepository.deleteAllById(ids)
    }
}
