package com.yorizori.yoremo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YoremoApplication

fun main(args: Array<String>) {
	runApplication<YoremoApplication>(*args)
}
