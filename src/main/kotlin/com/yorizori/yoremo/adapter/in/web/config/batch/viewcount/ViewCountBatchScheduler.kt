package com.yorizori.yoremo.adapter.`in`.web.config.batch.viewcount

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.YearMonth

@Component
class ViewCountBatchScheduler(
    private val jobLauncher: JobLauncher,
    private val viewCountJob: Job,
    private val jobExplorer: JobExplorer
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 0 3 1 * *")
    fun runMonthlyViewCountBatch() {
        val lastMonth = YearMonth.now().minusMonths(1)
        logger.info("월간 조회수 배치 실행 - 대상: $lastMonth")

        runViewCountBatch(lastMonth, "monthly")
    }

    @Scheduled(cron = "0 0 4 * * *")
    fun checkAndRetryFailedBatch() {
        logger.info("조회수 배치 실패 확인 시작")

        val recentMonths = (1..3).map { YearMonth.now().minusMonths(it.toLong()) }

        recentMonths.forEach { month ->
            if (needsRetry(month)) {
                logger.info("$month 배치 재시도 실행")
                runViewCountBatch(month, "retry")
            }
        }
    }

    private fun runViewCountBatch(targetMonth: YearMonth, jobType: String) {
        try {
            val jobParameters = JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("targetMonth", targetMonth.toString()) // 2024-05 형식
                .addString("jobType", jobType)
                .toJobParameters()

            val jobExecution = jobLauncher.run(viewCountJob, jobParameters)

            logger.info("조회수 배치 실행 - 대상월: $targetMonth, 타입: $jobType, 실행ID: ${jobExecution.id}")
        } catch (e: Exception) {
            logger.error("조회수 배치 실행 실패 - 대상월: $targetMonth, 타입: $jobType", e)
        }
    }

    private fun needsRetry(targetMonth: YearMonth): Boolean {
        val jobInstances = jobExplorer.getJobInstances("viewCountJob", 0, 50)

        for (jobInstance in jobInstances) {
            val jobExecutions = jobExplorer.getJobExecutions(jobInstance)

            for (jobExecution in jobExecutions) {
                val jobTargetMonth = jobExecution.jobParameters.getString("targetMonth")

                if (jobTargetMonth == targetMonth.toString()) {
                    when (jobExecution.status) {
                        BatchStatus.COMPLETED -> {
                            logger.info("$targetMonth 배치 이미 성공 완료")
                            return false
                        }
                        BatchStatus.FAILED -> {
                            logger.warn("$targetMonth 배치 실패 발견 - 재시도 필요")
                            continue
                        }
                        BatchStatus.STARTED -> {
                            logger.info("$targetMonth 배치 실행 중")
                            return false
                        }
                        else -> continue
                    }
                }
            }
        }

        logger.info("$targetMonth 배치 성공 기록 없음 - 재시도 필요")
        return true
    }
}
