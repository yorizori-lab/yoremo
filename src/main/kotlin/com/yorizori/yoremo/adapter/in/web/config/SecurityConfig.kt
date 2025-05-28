package com.yorizori.yoremo.adapter.`in`.web.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.yorizori.yoremo.adapter.`in`.web.constant.YoremoHttpUrl
import com.yorizori.yoremo.adapter.`in`.web.filter.LoginAuthenticationFilter
import com.yorizori.yoremo.adapter.`in`.web.filter.RegisterAuthenticationFilter
import com.yorizori.yoremo.adapter.`in`.web.oauth2.OAuth2FailureHandler
import com.yorizori.yoremo.adapter.out.redis.RedisTokenService
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oauth2SuccessHandler: OAuth2SuccessHandler,
    private val oauth2FailureHandler: OAuth2FailureHandler,
    private val yoremoHttpUrl: YoremoHttpUrl,
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender,
    private val redisTokenService: RedisTokenService,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(yoremoHttpUrl.frontendBaseUrl)
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun loginAuthenticationFilter(): LoginAuthenticationFilter {
        return LoginAuthenticationFilter(
            usersRepository = usersRepository,
            passwordEncoder = passwordEncoder(),
            objectMapper = objectMapper
        ).apply {
            setAuthenticationManager(authenticationManager())
            setRequiresAuthenticationRequestMatcher { request ->
                request.servletPath == "/api/users/v1/login" && request.method == "POST"
            }
        }
    }

    @Bean
    fun registerAuthenticationFilter(): RegisterAuthenticationFilter {
        return RegisterAuthenticationFilter(
            usersRepository,
            emailSender,
            passwordEncoder(),
            redisTokenService,
            objectMapper
        ).apply {
            setAuthenticationManager(authenticationManager())
            setRequiresAuthenticationRequestMatcher { request ->
                request.servletPath == "/api/users/v1/register" && request.method == "POST"
            }
        }
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager(
            listOf(
                object : AuthenticationProvider {
                    override fun authenticate(authentication: Authentication): Authentication {
                        return authentication
                    }

                    override fun supports(authentication: Class<*>): Boolean {
                        return true
                    }
                }
            )
        )
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
                    .requestMatchers("/api/users/v1/register").permitAll()
                    .requestMatchers("/api/users/v1/login").permitAll()
                    .requestMatchers("/api/users/v1/verify-email").permitAll()
                    .requestMatchers("/api/users/v1/resend-verification").permitAll()
                    .requestMatchers("/api/users/v1/check-email").permitAll()
                    // 레시피
                    .requestMatchers(HttpMethod.GET, "/api/recipes/v1/recipes").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/recipes/v1/recipes/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/recipes/v1/recipes/search").permitAll()
                    // 카테고리
                    .requestMatchers("/api/categories/v1/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterAt(
                loginAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .addFilterAt(
                registerAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(oauth2SuccessHandler)
                    .failureHandler(oauth2FailureHandler)
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

        return http.build()
    }
}
