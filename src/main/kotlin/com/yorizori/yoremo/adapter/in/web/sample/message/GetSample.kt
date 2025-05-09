package com.yorizori.yoremo.adapter.`in`.web.sample.message

import java.time.Instant

abstract class GetSample {

    data class PathVariable(val id: Long)

    data class Response(
        val id: Long,
        val message: String,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
