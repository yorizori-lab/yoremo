package com.yorizori.yoremo.adapter.`in`.web.config

import com.yorizori.yoremo.adapter.`in`.web.filter.LoginFilter
import com.yorizori.yoremo.domain.users.service.LoginService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(
        http: HttpSecurity,
        corsConfigurationSource: CorsConfigurationSource,
        loginService: LoginService
    ): SecurityFilterChain {
        return http
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource)
            }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .securityContext { securityContext ->
                securityContext.requireExplicitSave(false)
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                session.maximumSessions(1)
                session.sessionFixation().migrateSession()
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/api/users/v1/send-verification",
                        "/api/users/v1/verify-email",
                        "/api/users/v1/register",
                        "/api/users/v1/login"
                    ).permitAll()
                    .requestMatchers("/ping").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                LoginFilter(loginService),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .build()
    }
}
