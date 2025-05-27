package com.yorizori.yoremo.adapter.`in`.web.config

import com.yorizori.yoremo.adapter.`in`.web.oauth2.OAuth2FailureHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oauth2SuccessHandler: OAuth2SuccessHandler,
    private val oauth2FailureHandler: OAuth2FailureHandler
) {

    @Value("\${app.frontend.base-url}")
    private lateinit var frontendBaseUrl: String

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(frontendBaseUrl)
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { session ->
                session.maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/ping").permitAll()
                    .requestMatchers("/api/users/v1/signup").permitAll()
                    .requestMatchers("/api/users/v1/login").permitAll()
                    .requestMatchers("/api/users/v1/verify-email").permitAll()
                    .requestMatchers("/api/users/v1/resend-verification").permitAll()
                    // 레시피
                    .requestMatchers("/api/recipes/v1/recipes").permitAll()
                    .requestMatchers("/api/recipes/v1/recipes/*").permitAll()
                    .requestMatchers("/api/recipes/v1/recipes/search").permitAll()
                    // 카테고리
                    .requestMatchers("/api/categories/v1/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(oauth2SuccessHandler) // 소셜 로그인 성공 핸들러
                    .failureHandler(oauth2FailureHandler) // 소셜 로그인 실패 핸들러
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

        return http.build()
    }
}
