package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.domain.users.entity.SocialAccounts
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.EmailSender
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class OAuth2UserService(
    private val usersRepository: UsersRepository,
    private val emailSender: EmailSender
) {
    @Transactional
    fun processOAuth2User(
        email: String,
        name: String,
        provider: SocialAccounts.Provider,
        providerId: String,
        profileImageUrl: String? = null
    ): Users {
        // 1. 기존 소셜 계정 확인
        val existingSocialAccount = usersRepository.findSocialAccountByProviderAndProviderId(
            provider,
            providerId
        )

        if (existingSocialAccount != null) {
            // 기존 소셜 계정이 있음 → 해당 사용자로 로그인
            return handleExistingSocialAccount(existingSocialAccount, profileImageUrl)
        }

        // TODO: 수정 해야됨
        // 2. 이메일로 기존 사용자 확인
        val existingUser = usersRepository.findByEmail(email)

        return if (existingUser != null) {
            // 기존 사용자에 새 소셜 계정 연결
            linkSocialAccountToExistingUser(
                existingUser,
                provider,
                providerId,
                email,
                profileImageUrl
            )
        } else {
            // 신규 사용자
            registerUserWithSocialAccount(email, name, provider, providerId, profileImageUrl)
        }
    }

    private fun handleExistingSocialAccount(
        socialAccount: SocialAccounts,
        profileImageUrl: String?
    ): Users {
        val user = usersRepository.findById(socialAccount.userId)!!

        val updatedUser = if (profileImageUrl != null && user.profileImageUrl != profileImageUrl) {
            user.copy(profileImageUrl = profileImageUrl)
        } else {
            user
        }

        val loggedInUser = updatedUser.copy(lastLoginAt = Instant.now())
        return usersRepository.save(loggedInUser)
    }

    private fun linkSocialAccountToExistingUser(
        existingUser: Users,
        provider: SocialAccounts.Provider,
        providerId: String,
        providerEmail: String,
        profileImageUrl: String?
    ): Users {
        val socialAccount = SocialAccounts(
            userId = existingUser.userId!!,
            provider = provider,
            providerId = providerId,
            providerEmail = providerEmail
        )
        usersRepository.saveSocialAccount(socialAccount)

        val updatedUser = existingUser.copy(
            profileImageUrl = profileImageUrl ?: existingUser.profileImageUrl,
            lastLoginAt = Instant.now()
        )

        return usersRepository.save(updatedUser)
    }

    private fun registerUserWithSocialAccount(
        email: String,
        name: String,
        provider: SocialAccounts.Provider,
        providerId: String,
        profileImageUrl: String?
    ): Users {
        val newUser = Users(
            email = email,
            password = null,
            name = name,
            profileImageUrl = profileImageUrl,
            verificationToken = null,
            tokenExpiresAt = null,
            lastLoginAt = Instant.now()
        )

        val savedUser = usersRepository.save(newUser)

        val socialAccount = SocialAccounts(
            userId = savedUser.userId!!,
            provider = provider,
            providerId = providerId,
            providerEmail = email
        )
        usersRepository.saveSocialAccount(socialAccount)

        emailSender.sendWelcomeEmail(savedUser.email, savedUser.name)

        return savedUser
    }
}
