package com.yorizori.yoremo.adapter.out.redis.email

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisVerificationEmailAdapter(
    private val redisTemplate: RedisTemplate<String, String>
) : RedisVerificationEmailRepository {

    override fun setEmailCode(email: String, code: String, duration: Duration) {
        redisTemplate.opsForValue().set(email, code, duration)
    }

    override fun getEmailCode(email: String): String? {
        return redisTemplate.opsForValue().get(email)
    }

    override fun deleteEmailCode(email: String) {
        redisTemplate.delete(email)
    }

    override fun setEmailVerified(email: String, duration: Duration) {
        redisTemplate.opsForValue().set("$email:verified", "true", duration)
    }

    override fun isEmailVerified(email: String): Boolean {
        return redisTemplate.opsForValue().get("$email:verified") == "true"
    }

    override fun deleteEmailVerified(email: String) {
        redisTemplate.delete("$email:verified")
    }
}
