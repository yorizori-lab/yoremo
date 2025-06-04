package com.yorizori.yoremo.adapter.`in`.web.config.batch

import com.yorizori.yoremo.adapter.out.persistence.foods.FoodsAdapter
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FoodVectorSyncScheduler(
    private val jobLauncher: JobLauncher,
    private val foodVectorSyncJob: Job,
    private val foodsAdapter: FoodsAdapter
) {

    @Scheduled(cron = "0 0 2 * * *")
    fun runFoodVectorSync() {
        try {
            val needingSyncCount = foodsAdapter.countFoodsNeedingVectorSync()

            if (needingSyncCount == 0L) return

            val jobParameters = JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addLong("needingSyncCount", needingSyncCount)
                .toJobParameters()

            jobLauncher.run(foodVectorSyncJob, jobParameters)
        } catch (_: Exception) {
        }
    }

    fun runManualSync(): String {
        return try {
            runFoodVectorSync()
            "Manual sync completed"
        } catch (e: Exception) {
            "Manual sync failed: ${e.message}"
        }
    }
}
