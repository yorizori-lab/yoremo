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

    // TODO: redis 에 저장하도록 변경 (임시값)
    // key: 인증토큰, value: userId
    val verificationToken: String? = null,

    // TODO: redis 에 저장하도록 변경 (임시값)
    val tokenExpiresAt: Instant? = null,

    val lastLoginAt: Instant? = null,

    /**
     * 소셜로그인을 안했다
     * - email password 로 회원가입
     * - 이메일 인증 받음
     * - email, password 저장
     *
     * 소셜로그인을 했다
     * - email 저장안해
     * - socialEmail 저장
     * - providerId 저장
     *
     * TODO: 수정 필요함
     */
    @OneToMany(mappedBy = "userId")
    var socialAccounts: MutableList<SocialAccounts> = mutableListOf()

) : BaseEntity() {

    enum class Role(val description: String) {
        USER("사용자"),
        ADMIN("관리자")
    }
}
