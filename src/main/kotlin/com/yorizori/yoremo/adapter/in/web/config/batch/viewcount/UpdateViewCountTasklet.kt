package com.yorizori.yoremo.adapter.`in`.web.config.batch.viewcount

import com.yorizori.yoremo.domain.recipes.port.RecipesRepository
import com.yorizori.yoremo.domain.recipeviewlogs.port.RecipeViewLogsRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.YearMonth

@Component
class UpdateViewCountTasklet(
    private val recipeViewLogsRepository: RecipeViewLogsRepository,
    private val recipesRepository: RecipesRepository
) : Tasklet {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        val targetMonth = getTargetMonth(chunkContext)
        logger.info("조회수 배치 시작 - 대상 월: $targetMonth")

        try {
            val monthStart = targetMonth.atDay(1).atStartOfDay()
            val monthEnd = targetMonth.atEndOfMonth().plusDays(1).atStartOfDay()

            val viewCounts = recipeViewLogsRepository.getViewCountsBetweenDates(
                monthStart,
                monthEnd
            )

            var updateCount = 0

            viewCounts.forEach { (recipeId, count) ->
                recipesRepository.incrementViewCountBy(recipeId, count)
                updateCount++
            }

            logger.info(
                "조회수 업데이트 완료 - 대상월: " +
                    "$targetMonth, 레시피: ${updateCount}개, 총 조회수: ${viewCounts.values.sum()}"
            )
        } catch (e: Exception) {
            logger.error("조회수 업데이트 실패 - 대상월: $targetMonth", e)
            throw e
        }

        return RepeatStatus.FINISHED
    }

    private fun getTargetMonth(chunkContext: ChunkContext): YearMonth {
        val jobParameters = chunkContext.stepContext.jobParameters
        val targetMonthStr = jobParameters["targetMonth"]?.toString()

        return if (targetMonthStr != null) {
            YearMonth.parse(targetMonthStr) // "2024-05" 형식
        } else {
            YearMonth.now().minusMonths(1) // 지난 달
        }
    }
}
