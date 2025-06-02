package com.yorizori.yoremo.adapter.out.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisUtils(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private val DURATION_TIME = Duration.ofMinutes(5) // 5분

    fun setEmailCode(email: String, code: String) {
        redisTemplate.opsForValue().set(email, code, DURATION_TIME)
    }

    fun getEmailCode(email: String): String? {
        val key = redisTemplate.opsForValue().get(email)
        println("데이터 확인" + key)
        return key
    }

    fun deleteEmailCode(email: String) {
        redisTemplate.delete(email)
    }

    fun setEmailVerified(email: String) {
        redisTemplate.opsForValue().set("$email:verified", "true", DURATION_TIME)
    }

    fun isEmailVerified(email: String): Boolean {
        return redisTemplate.opsForValue().get("$email:verified") == "true"
    }

    fun deleteEmailVerified(email: String) {
        redisTemplate.delete("$email:verified")
    }
}
