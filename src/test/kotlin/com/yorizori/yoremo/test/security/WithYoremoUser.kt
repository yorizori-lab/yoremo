package com.yorizori.yoremo.test.security

import org.springframework.security.test.context.support.WithSecurityContext

@WithSecurityContext(factory = WithYoremoUserSecurityContextFactory::class)
annotation class WithYoremoUser(
    val userId: Long = 1L,
    val email: String = "test@test.com",
    val password: String = "test",
    val authority: String = "ROLE_USER"
)
