package com.yorizori.yoremo.adapter.out.persistence.mealrecords

import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface MealRecordsJpaRepository : JpaRepository<MealRecords, Long> {

    fun findByUserIdAndMealDateBetweenOrderByMealDateAsc(
        userId: Long,
        startDate: Instant,
        endDate: Instant
    ): List<MealRecords>
}
