package com.yorizori.yoremo.adapter.`in`.web.sample

import com.yorizori.yoremo.adapter.`in`.web.sample.message.CreateSample
import com.yorizori.yoremo.adapter.`in`.web.sample.message.GetSample
import com.yorizori.yoremo.domain.sample.service.CreateSampleService
import com.yorizori.yoremo.domain.sample.service.GetSampleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sample/v1")
class SampleController(
    private val getSampleService: GetSampleService,
    private val createSampleService: CreateSampleService
) {
    @GetMapping("/samples/{id}")
    fun get(
        request: GetSample.PathVariable
    ): GetSample.Response {
        return getSampleService.getSample(request.id)
    }

    @PostMapping("/samples")
    fun create(
        @RequestBody request: CreateSample.Request
    ): CreateSample.Response {
        return createSampleService.create(request)
    }
}
