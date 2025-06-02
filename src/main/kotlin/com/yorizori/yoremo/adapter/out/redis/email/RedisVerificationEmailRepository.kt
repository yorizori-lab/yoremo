package com.yorizori.yoremo.adapter.out.redis.email

import java.time.Duration

interface RedisVerificationEmailRepository {
    fun setEmailCode(email: String, code: String, duration: Duration)
    fun getEmailCode(email: String): String?
    fun deleteEmailCode(email: String)
    fun setEmailVerified(email: String, duration: Duration)
    fun isEmailVerified(email: String): Boolean
    fun deleteEmailVerified(email: String)
}
