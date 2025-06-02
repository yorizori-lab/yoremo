package com.yorizori.yoremo.adapter.`in`.web.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.yorizori.yoremo.adapter.`in`.web.config.security.authentication.YoremoAuthenticationFilter
import com.yorizori.yoremo.adapter.`in`.web.config.security.authentication.YoremoDevAuthorizationFilter
import com.yorizori.yoremo.adapter.`in`.web.config.security.authentication.YoremoUserDetailsService
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.cors.CorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val usersRepository: UsersRepository,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun securityContextRepository(): SecurityContextRepository {
        return HttpSessionSecurityContextRepository()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        environment: Environment,
        corsConfigurationSource: CorsConfigurationSource,
        yoremoUserDetailsService: YoremoUserDetailsService,
        authenticationManager: AuthenticationManager,
        securityContextRepository: SecurityContextRepository
    ): SecurityFilterChain {
        val http = http
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource)
            }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                    .maximumSessions(1)
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
            .logout {
                it
                    .logoutUrl("/api/users/v1/logout")
                    .logoutSuccessHandler { request, response, authentication ->
                        request.session.invalidate()
                        response.status = 200
                    }
            }
            .userDetailsService(yoremoUserDetailsService)
            .addFilterBefore(
                YoremoAuthenticationFilter(
                    securityContextRepository,
                    authenticationManager,
                    objectMapper
                ),
                UsernamePasswordAuthenticationFilter::class.java
            )

        if (environment.matchesProfiles("local")) {
            http.addFilterBefore(
                YoremoDevAuthorizationFilter(usersRepository),
                AuthorizationFilter::class.java
            )
        }

        return http.build()
    }
}
