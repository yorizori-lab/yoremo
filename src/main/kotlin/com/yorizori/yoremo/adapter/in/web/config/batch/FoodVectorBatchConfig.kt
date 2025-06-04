package com.yorizori.yoremo.adapter.`in`.web.config.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class FoodVectorBatchConfig {

    @Bean
    fun foodVectorSyncJob(
        jobRepository: JobRepository,
        foodVoctorSyncStep: Step
    ): Job {
        return JobBuilder("foodVectorSyncJob", jobRepository)
            .start(foodVoctorSyncStep)
            .build()
    }

    @Bean
    fun foodVectorSyncStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        foodVectorSyncTasklet: FoodVectorSyncTasklet
    ): Step {
        return StepBuilder("foodVectorSyncStep", jobRepository)
            .tasklet(foodVectorSyncTasklet, transactionManager)
            .build()
    }
}
