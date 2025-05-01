package com.yorizori.yoremo.domain.sample.service

import com.yorizori.yoremo.application.message.sample.CreateSample
import com.yorizori.yoremo.domain.sample.entity.Sample
import com.yorizori.yoremo.domain.sample.port.SampleRepository
import org.springframework.stereotype.Service

@Service
class CreateSampleService(
    private val sampleRepository: SampleRepository
) {

    fun create(request: CreateSample.Request): CreateSample.Response {
        val saved = sampleRepository.create(Sample(message = request.message))
        return CreateSample.Response(saved.id!!)
    }
}
