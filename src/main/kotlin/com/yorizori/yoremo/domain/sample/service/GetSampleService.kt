package com.yorizori.yoremo.domain.sample.service

import com.yorizori.yoremo.adapter.`in`.web.sample.message.GetSample
import com.yorizori.yoremo.domain.sample.port.SampleRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class GetSampleService(
    private val sampleRepository: SampleRepository
) {
    @Transactional(readOnly = true)
    fun getSample(id: Long): GetSample.Response {
        val sample = sampleRepository.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "sample not found with id: $id"
            )

        return GetSample.Response(
            id = sample.id!!,
            message = sample.message,
            createdAt = sample.createdAt,
            updatedAt = sample.updatedAt
        )
    }
}
