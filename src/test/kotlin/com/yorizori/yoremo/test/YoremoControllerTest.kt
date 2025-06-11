package com.yorizori.yoremo.test

import com.yorizori.yoremo.test.security.TestSecurityConfig
import org.springframework.context.annotation.Import

@Import(TestSecurityConfig::class)
annotation class YoremoControllerTest
