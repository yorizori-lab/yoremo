package com.yorizori.yoremo.domain.users.service

import com.yorizori.yoremo.adapter.`in`.web.users.message.GetMe
import com.yorizori.yoremo.domain.users.entity.Users
import com.yorizori.yoremo.domain.users.port.UsersRepository
import org.springframework.stereotype.Service

@Service
class GetMeService(
    private val usersRepository: UsersRepository
) {

    fun getMe(currentUser: Users): GetMe.Response {
        val socialAccounts = usersRepository.findSocialAccountsByUserId(currentUser.userId!!)

        val socialAccountInfos = socialAccounts.map { socialAccount ->
            GetMe.SocialAccountInfo(
                provider = socialAccount.provider,
                providerEmail = socialAccount.providerEmail
            )
        }

        return GetMe.Response(
            userId = currentUser.userId!!,
            email = currentUser.email,
            name = currentUser.name,
            role = currentUser.role,
            profileImageUrl = currentUser.profileImageUrl,
            isLocalUser = currentUser.password != null,
            socialAccounts = socialAccountInfos
        )
    }
}
