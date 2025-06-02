package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.out.redis.RedisTokenService
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
    private val emailSender: EmailSender,
    private val redisTokenService: RedisTokenService
) {
    @Transactional
    fun processOAuth2User(
        email: String,
        name: String,
        provider: SocialAccounts.Provider,
        providerId: String,
        profileImageUrl: String? = null
    ): Users {
        val existingSocialAccount = usersRepository.findSocialAccountByProviderAndProviderId(
            provider,
            providerId
        )

        if (existingSocialAccount != null) {
            return handleExistingSocialAccount(existingSocialAccount)
        }

        val existingUser = usersRepository.findByEmail(email)

        return if (existingUser != null) {
            linkSocialAccountToExistingUser(
                existingUser,
                provider,
                providerId,
                email
            )
        } else {
            registerUserWithSocialAccount(email, name, provider, providerId, profileImageUrl)
        }
    }

    private fun handleExistingSocialAccount(socialAccount: SocialAccounts): Users {
        val user = usersRepository.findById(socialAccount.userId)!!

        val loggedInUser = user.copy(lastLoginAt = Instant.now())
        return usersRepository.save(loggedInUser)
    }

    private fun linkSocialAccountToExistingUser(
        existingUser: Users,
        provider: SocialAccounts.Provider,
        providerId: String,
        providerEmail: String
    ): Users {
        val socialAccount = SocialAccounts(
            userId = existingUser.userId!!,
            provider = provider,
            providerId = providerId,
            providerEmail = providerEmail
        )
        usersRepository.saveSocialAccount(socialAccount)

        redisTokenService.deleteToken(existingUser.email)

        /*
        * 회원가입 후, 이메일 인증이 안된 상태로
        * 소셜 로그인을 시도한 경우, 이메일 확인으로 변경
         */
        val updatedUser = existingUser.copy(
            isEmailVerified = true,
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
            isEmailVerified = true,
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
