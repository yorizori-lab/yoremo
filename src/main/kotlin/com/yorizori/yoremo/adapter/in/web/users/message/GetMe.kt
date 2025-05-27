package com.yorizori.yoremo.adapter.`in`.web.users.message

import com.yorizori.yoremo.domain.users.entity.SocialAccounts
import com.yorizori.yoremo.domain.users.entity.Users

abstract class GetMe {

    data class Response(
        val userId: Long,
        val email: String,
        val name: String,
        val role: Users.Role,
        val profileImageUrl: String?,
        val isLocalUser: Boolean,
        val socialAccounts: List<SocialAccountInfo>
    )

    data class SocialAccountInfo(
        val provider: SocialAccounts.Provider,
        val providerEmail: String?
    )
}
