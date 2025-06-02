package com.yorizori.yoremo.adapter.`in`.web.config

import com.yorizori.yoremo.adapter.`in`.web.constant.YoremoHttpUrl
import com.yorizori.yoremo.domain.users.entity.SocialAccounts
import com.yorizori.yoremo.domain.users.service.OAuth2UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class OAuth2SuccessHandler(
    private val oauth2UserService: OAuth2UserService,
    private val yoremoHttpUrl: YoremoHttpUrl
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauth2User = authentication.principal as OAuth2User

        try {
            val provider = mapToProvider(request)
            val userInfo = extractUserInfo(oauth2User, provider)

            val user = oauth2UserService.processOAuth2User(
                email = userInfo.email,
                name = userInfo.name,
                provider = provider,
                providerId = userInfo.providerId,
                profileImageUrl = userInfo.profileImageUrl
            )

            // 이렇게 해도 @AuthenticationPrincipal 어노테이션을 통해 유저 식별 가능한지 확인
            request.session.setAttribute("user", user)

            response.sendRedirect(yoremoHttpUrl.oAuthSuccessUrl)
        } catch (e: Exception) {
            // 에러 발생 시 실패 페이지로 리다이렉트
            response.sendRedirect(
                yoremoHttpUrl.oAuthFailureUrl(
                    message = URLEncoder.encode(e.message, Charsets.UTF_8)
                )
            )
        }
    }

    private fun mapToProvider(request: HttpServletRequest): SocialAccounts.Provider {
        val registrationId = request.requestURI.substringAfterLast("/")

        return when (registrationId.lowercase()) {
            "google" -> SocialAccounts.Provider.GOOGLE
            "kakao" -> SocialAccounts.Provider.KAKAO
            "naver" -> SocialAccounts.Provider.NAVER
            else -> throw IllegalArgumentException("지원하지 않는 OAuth2 제공자: $registrationId")
        }
    }

    private fun extractUserInfo(
        oauth2User: OAuth2User,
        provider: SocialAccounts.Provider
    ): UserInfo {
        return when (provider) {
            SocialAccounts.Provider.GOOGLE -> extractGoogleUserInfo(oauth2User)
            SocialAccounts.Provider.KAKAO -> extractKakaoUserInfo(oauth2User)
            SocialAccounts.Provider.NAVER -> extractNaverUserInfo(oauth2User)
        }
    }

    private fun extractGoogleUserInfo(oauth2User: OAuth2User): UserInfo {
        return UserInfo(
            email = oauth2User.getAttribute("email") ?: throw IllegalArgumentException(
                "Google 이메일 정보 없음"
            ),
            name = oauth2User.getAttribute("name") ?: "구글 사용자",
            providerId = oauth2User.getAttribute("sub") ?: throw IllegalArgumentException(
                "Google ID 없음"
            ),
            profileImageUrl = oauth2User.getAttribute("picture")
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractKakaoUserInfo(oauth2User: OAuth2User): UserInfo {
        val kakaoAccount = oauth2User.getAttribute<Map<String, Any>>("kakao_account")
            ?: throw IllegalArgumentException("카카오 계정 정보 없음")
        val profile = kakaoAccount["profile"] as? Map<String, Any>

        return UserInfo(
            email = kakaoAccount["email"] as? String ?: throw IllegalArgumentException(
                "카카오 이메일 정보 없음"
            ),
            name = profile?.get("nickname") as? String ?: "카카오 사용자",
            providerId = oauth2User.getAttribute<Long>("id")
                ?.toString() ?: throw IllegalArgumentException(
                "카카오 ID 없음"
            ),
            profileImageUrl = profile?.get("profile_image_url") as? String
        )
    }

    private fun extractNaverUserInfo(oauth2User: OAuth2User): UserInfo {
        val response = oauth2User.getAttribute<Map<String, Any>>("response")
            ?: throw IllegalArgumentException("네이버 응답 정보 없음")

        return UserInfo(
            email = response["email"] as? String ?: throw IllegalArgumentException("네이버 이메일 정보 없음"),
            name = response["name"] as? String ?: "네이버 사용자",
            providerId = response["id"] as? String ?: throw IllegalArgumentException("네이버 ID 없음"),
            profileImageUrl = response["profile_image"] as? String
        )
    }

    private data class UserInfo(
        val email: String,
        val name: String,
        val providerId: String,
        val profileImageUrl: String?
    )
}
