package com.yorizori.yoremo.adapter.`in`.web.config.security

import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class YoremoAuthentication(
    private val email: String,
    private val password: String,
    val userId: Long,
    val authority: GrantedAuthority
) : UserDetails, AuthenticatedPrincipal {

    override fun getAuthorities(): Collection<GrantedAuthority?>? = listOf(authority)

    override fun getPassword(): String? = password

    override fun getUsername(): String? = email

    override fun toString(): String {
        return "YoremoAuthentication(email='$email', userId=$userId, authority=$authority)"
    }

    override fun getName(): String? = email
}
