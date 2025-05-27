package com.yorizori.yoremo.domain.users.entity

import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "social_accounts")
data class SocialAccounts(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val socialAccountId: Long? = null,

    val userId: Long,

    @Enumerated(EnumType.STRING)
    val provider: Provider,

    val providerId: String,

    val providerEmail: String? = null
) : BaseEntity() {

    enum class Provider(val description: String) {
        GOOGLE("구글"),
        KAKAO("카카오"),
        NAVER("네이버")
    }
}
