package com.yorizori.yoremo.domain.users.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users")
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long? = null,

    val email: String,

    val password: String? = null,

    val name: String,

    val profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER,

    val lastLoginAt: Instant? = null,

    val isEmailVerified: Boolean = false,

    @OneToMany(mappedBy = "userId")
    var socialAccounts: MutableList<SocialAccounts> = mutableListOf()

) : BaseEntity() {

    enum class Role(val description: String) {
        USER("사용자"),
        ADMIN("관리자")
    }
}
