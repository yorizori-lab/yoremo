package com.yorizori.yoremo.adapter.`in`.web.config.batch.viewcount

import com.yorizori.yoremo.domain.recipeviewlogs.port.RecipeViewLogsRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CleanupViewLogsTasklet(
    private val recipeViewLogsRepository: RecipeViewLogsRepository
) : Tasklet {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        try {
            val today = LocalDate.now()
            val currentMonthStart = today.withDayOfMonth(1)
            val cutoffDate = currentMonthStart.atStartOfDay()

            logger.info("조회 로그 정리 시작 - 삭제 기준: $currentMonthStart 이전 (현재 월 이전 모든 데이터)")

            val deletedCount = recipeViewLogsRepository.deleteLogsBeforeDate(cutoffDate)

            logger.info("조회 로그 정리 완료 - 삭제된 로그: ${deletedCount}건")
        } catch (e: Exception) {
            logger.error("조회 로그 정리 실패", e)
            throw e
        }

        return RepeatStatus.FINISHED
    }
}
