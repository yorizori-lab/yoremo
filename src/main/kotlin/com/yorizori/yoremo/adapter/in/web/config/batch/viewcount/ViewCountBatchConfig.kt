package com.yorizori.yoremo.adapter.`in`.web.config.batch.viewcount

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ViewCountBatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager
) {

    @Bean
    fun viewCountJob(
        updateViewCountStep: Step,
        cleanupViewLogsStep: Step
    ): Job {
        return JobBuilder("viewCountJob", jobRepository)
            .start(updateViewCountStep)
            .next(cleanupViewLogsStep)
            .build()
    }

    @Bean
    fun updateViewCountStep(
        updateViewCountTasklet: UpdateViewCountTasklet
    ): Step {
        return StepBuilder("updateViewCountStep", jobRepository)
            .tasklet(updateViewCountTasklet, transactionManager)
            .build()
    }

    @Bean
    fun cleanupViewLogsStep(
        cleanupViewLogsTasklet: CleanupViewLogsTasklet
    ): Step {
        return StepBuilder("cleanupViewLogsStep", jobRepository)
            .tasklet(cleanupViewLogsTasklet, transactionManager)
            .build()
    }
}
