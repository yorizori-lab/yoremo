package com.yorizori.yoremo.adapter.`in`.web.security

import com.yorizori.yoremo.domain.users.entity.Users
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserPrincipal(
    private val users: Users
) : UserDetails {
    fun getUsers(): Users = users

    fun getUserId(): Long? = users.userId!!

    fun getEmail(): String = users.email

    fun getName(): String = users.name

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(SimpleGrantedAuthority("ROLE_${users.role.name}"))
    }

    override fun getPassword(): String? {
        return users.password
    }

    override fun getUsername(): String? {
        return users.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return users.isEmailVerified // true면 활성화, false면 비활성화
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomUserPrincipal) return false
        return users.userId == other.users.userId
    }

    override fun hashCode(): Int {
        return users.userId?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "CustomUserPrincipal(userId=${users.userId}, email=${users.email})"
    }
}
