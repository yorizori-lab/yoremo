package com.yorizori.yoremo.adapter.out.persistence.recipeviewlogs

import com.yorizori.yoremo.domain.recipeviewlogs.entity.RecipeViewLogs
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeViewLogsJpaRepository : JpaRepository<RecipeViewLogs, Long>
