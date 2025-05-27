package com.yorizori.yoremo.adapter.out.persistence.users

import com.yorizori.yoremo.domain.users.entity.SocialAccounts
import org.springframework.data.jpa.repository.JpaRepository

@Deprecated("삭제")
interface SocialAccountsJpaRepository : JpaRepository<SocialAccounts, Long> {
    fun findByProviderAndProviderId(
        provider: SocialAccounts.Provider,
        providerId: String
    ): SocialAccounts?
    fun findByUserId(userId: Long): List<SocialAccounts>
}
