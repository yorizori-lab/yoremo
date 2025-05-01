package com.yorizori.yoremo.adapter.postgresql.sample

import com.yorizori.yoremo.model.Sample
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class SampleAdapter(
    private val sampleRepository: SampleRepository
) {

    fun findById(id: Long): Sample? {
        return sampleRepository.findById(id).getOrNull()?.toModel()
    }

    fun create(sample: Sample): Sample {
        return sampleRepository.save(SampleEntity.from(sample)).toModel()
    }
}
