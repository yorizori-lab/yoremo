package com.yorizori.yoremo.model

import java.time.Instant

data class Sample(
    val id: Long? = null,
    val message: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
