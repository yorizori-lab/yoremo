package com.yorizori.yoremo.domain.mealrecords.port

import com.yorizori.yoremo.adapter.out.persistence.mealrecords.MealRecordsJapRepository
import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class MealRecordsRepository(
    private val mealRecordsJapRepository: MealRecordsJapRepository
) {

    fun findById(id: Long): MealRecords? {
        return mealRecordsJapRepository.findById(id).getOrNull()
    }

    fun save(mealRecords: MealRecords): MealRecords {
        return mealRecordsJapRepository.save(mealRecords)
    }

    fun saveAll(mealRecords: List<MealRecords>): List<MealRecords> {
        return mealRecordsJapRepository.saveAll(mealRecords)
    }

    fun deleteById(id: Long) {
        mealRecordsJapRepository.deleteById(id)
    }
}
