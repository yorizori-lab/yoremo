package com.yorizori.yoremo.adapter.`in`.web.config.batch.viewcount

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.YearMonth

@Component
class ViewCountBatchScheduler(
    private val jobLauncher: JobLauncher,
    private val viewCountJob: Job
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 0 3 1 * *")
    fun runMonthlyViewCountBatch() {
        val lastMonth = YearMonth.now().minusMonths(1)
        logger.info("월간 조회수 배치 실행 - 대상: $lastMonth")
        try {
            val jobParameters = JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("targetMonth", lastMonth.toString()) // 2024-05 형식
                .addString("jobType", "monthly")
                .toJobParameters()

            val jobExecution = jobLauncher.run(viewCountJob, jobParameters)

            logger.info("조회수 배치 실행 - 대상월: $lastMonth, 실행ID: ${jobExecution.id}")
        } catch (e: Exception) {
            logger.error("조회수 배치 실행 실패 - 대상월: $lastMonth", e)
        }
    }
}
