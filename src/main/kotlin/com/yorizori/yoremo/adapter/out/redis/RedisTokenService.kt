package com.yorizori.yoremo.adapter.out.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisTokenService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        private const val DEFAULT_TTL = 1800L // 30분
    }

    fun saveToken(email: String, token: String, ttlSeconds: Long = DEFAULT_TTL) {
        redisTemplate.opsForValue().set(email, token, Duration.ofSeconds(ttlSeconds))
    }

    fun getToken(email: String): String? {
        return redisTemplate.opsForValue().get(email)
    }

    fun verifyToken(email: String, token: String): Boolean {
        val storedToken = getToken(email)
        return storedToken != null && storedToken == token
    }

    fun deleteToken(email: String): Boolean {
        return redisTemplate.delete(email)
    }

    fun hasToken(email: String): Boolean {
        return redisTemplate.hasKey(email)
    }

    fun getTokenTTL(email: String): Long {
        return redisTemplate.getExpire(email)
    }
}
