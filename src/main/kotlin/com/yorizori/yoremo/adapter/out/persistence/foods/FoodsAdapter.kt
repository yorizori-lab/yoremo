package com.yorizori.yoremo.adapter.out.persistence.foods

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yorizori.yoremo.domain.foods.entity.Foods
import com.yorizori.yoremo.domain.foods.entity.QFoods.foods
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
class FoodsAdapter(
    private val queryFactory: JPAQueryFactory
) {

    fun findFoodsNeedingVectorSync(): List<Foods> {
        return queryFactory
            .selectFrom(foods)
            .where(
                foods.vectorSyncedAt.isNull
                    .or(foods.updatedAt.gt(foods.vectorSyncedAt))
            )
            .orderBy(foods.updatedAt.asc())
            .fetch()
    }

    fun countFoodsNeedingVectorSync(): Long {
        return queryFactory
            .select(foods.count())
            .from(foods)
            .where(
                foods.vectorSyncedAt.isNull
                    .or(foods.updatedAt.gt(foods.vectorSyncedAt))
            )
            .fetchOne() ?: 0L
    }

    @Transactional
    fun updateVectorSyncedAtOnly(foodId: Long, syncedAt: Instant): Long {
        return queryFactory
            .update(foods)
            .set(foods.vectorSyncedAt, syncedAt)
            .where(foods.foodId.eq(foodId))
            .execute()
    }
}
