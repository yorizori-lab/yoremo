package com.yorizori.yoremo.adapter.`in`.web.config.security.authentication

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class YoremoUserDetailsService(
    private val usersRepository: UsersRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails? {
        val user = usersRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("user not found with email: $email")

        return YoremoAuthentication(
            email = user.email,
            password = user.password!!,
            userId = user.userId!!,
            authority = SimpleGrantedAuthority("ROLE_${user.role}")
        )
    }
}
