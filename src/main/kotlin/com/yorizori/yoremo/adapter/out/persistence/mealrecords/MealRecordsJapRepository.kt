package com.yorizori.yoremo.adapter.out.persistence.mealrecords

import com.yorizori.yoremo.domain.mealrecords.entity.MealRecords
import org.springframework.data.jpa.repository.JpaRepository

interface MealRecordsJapRepository : JpaRepository<MealRecords, Long>
