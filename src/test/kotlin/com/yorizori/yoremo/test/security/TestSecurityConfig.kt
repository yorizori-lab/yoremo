package com.yorizori.yoremo.test.security

import com.yorizori.yoremo.adapter.`in`.web.config.security.SecurityConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@ConditionalOnMissingBean(value = [SecurityConfig::class])
@EnableWebSecurity
class TestSecurityConfig {

    /**
     * 테스트 환경에서는 모든 보안 설정을 비활성화한다.
     */
    @Bean
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
        return http.build()
    }
}
