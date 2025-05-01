package com.yorizori.yoremo.domain.sample.port

import com.yorizori.yoremo.adapter.out.persistence.sample.SampleJpaRepository
import com.yorizori.yoremo.domain.sample.entity.Sample
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
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
