package com.yorizori.yoremo.adapter.`in`.web.ping

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/ping")
    fun ping(): String {
        logger.info("ping request received. thread: ${Thread.currentThread()}")
        return "pong"
    }
}
