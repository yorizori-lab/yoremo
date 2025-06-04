package com.yorizori.yoremo.domain.users.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.time.Instant

@Entity
@DynamicUpdate
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

    @Enumerated(EnumType.STRING)
    val provider: Provider = Provider.LOCAL,

    val providerId: String? = null,

    val lastLoginAt: Instant? = null

) : BaseEntity() {

    enum class Role(val description: String) {
        USER("일반 사용자"),
        ADMIN("관리자");
    }

    enum class Provider(val description: String) {
        LOCAL("로컬 사용자"),
        GOOGLE("구글 사용자"),
        KAKAO("카카오 사용자"),
        NAVER("네이버 사용자")
    }
}
