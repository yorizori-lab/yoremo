package com.yorizori.yoremo.domain.sample.port

import com.yorizori.yoremo.adapter.out.persistence.sample.SampleJpaRepository
import com.yorizori.yoremo.domain.sample.entity.Sample
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class SampleRepository(
    private val sampleJpaRepository: SampleJpaRepository
) {

    fun findById(id: Long): Sample? {
        return sampleJpaRepository.findById(id).getOrNull()
    }

    fun create(sample: Sample): Sample {
        return sampleJpaRepository.save(sample)
    }
}
