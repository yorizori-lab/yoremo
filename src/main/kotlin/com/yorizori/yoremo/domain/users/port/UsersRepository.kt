package com.yorizori.yoremo.domain.users.port

import com.yorizori.yoremo.adapter.out.persistence.users.SocialAccountsJpaRepository
import com.yorizori.yoremo.adapter.out.persistence.users.UsersJpaRepository
import com.yorizori.yoremo.domain.users.entity.SocialAccounts
import com.yorizori.yoremo.domain.users.entity.Users
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class UsersRepository(
    private val usersJpaRepository: UsersJpaRepository,
    private val socialAccountsJpaRepository: SocialAccountsJpaRepository
) {
    fun findById(id: Long): Users? {
        return usersJpaRepository.findById(id).getOrNull()
    }

    fun findByEmail(email: String): Users? {
        return usersJpaRepository.findByEmail(email)
    }

    fun existsByEmail(email: String): Boolean {
        return usersJpaRepository.existsByEmail(email)
    }

    fun save(users: Users): Users {
        return usersJpaRepository.save(users)
    }

    fun deleteById(id: Long) {
        return usersJpaRepository.deleteById(id)
    }

    fun saveSocialAccount(socialAccount: SocialAccounts): SocialAccounts {
        return socialAccountsJpaRepository.save(socialAccount)
    }

    fun findSocialAccountByProviderAndProviderId(
        provider: SocialAccounts.Provider,
        providerId: String
    ): SocialAccounts? {
        return socialAccountsJpaRepository.findByProviderAndProviderId(provider, providerId)
    }

    fun findSocialAccountsByUserId(userId: Long): List<SocialAccounts> {
        return socialAccountsJpaRepository.findByUserId(userId)
    }
}
