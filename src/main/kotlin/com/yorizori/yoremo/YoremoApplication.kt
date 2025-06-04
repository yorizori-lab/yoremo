package com.yorizori.yoremo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class YoremoApplication

fun main(args: Array<String>) {
    runApplication<YoremoApplication>(*args)
}
