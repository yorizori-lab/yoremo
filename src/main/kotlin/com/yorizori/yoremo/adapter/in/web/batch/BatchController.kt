package com.yorizori.yoremo.adapter.`in`.web.batch

import com.yorizori.yoremo.adapter.`in`.web.config.batch.FoodVectorSyncScheduler
import com.yorizori.yoremo.adapter.out.persistence.foods.FoodsAdapter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/batch/v1")
class BatchController(
    private val foodVectorSyncScheduler: FoodVectorSyncScheduler,
    private val foodsAdapter: FoodsAdapter
) {

    @PostMapping("/food-vector/run")
    fun runFoodVectorSync(): Map<String, Any> {
        CompletableFuture.runAsync {
            foodVectorSyncScheduler.runManualSync()
        }
        return mapOf("message" to "Batch started in background")
    }

    @GetMapping("/food-vector/status")
    fun getFoodVectorSyncStatus(): Map<String, Any> {
        val needingSyncCount = foodsAdapter.countFoodsNeedingVectorSync()
        return mapOf(
            "needingSyncCount" to needingSyncCount,
            "message" to if (needingSyncCount > 0) {
                "$needingSyncCount foods need vector sync"
            } else {
                "All foods are synced"
            }
        )
    }
}
