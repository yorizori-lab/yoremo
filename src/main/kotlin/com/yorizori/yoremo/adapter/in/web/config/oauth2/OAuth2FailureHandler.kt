package com.yorizori.yoremo.adapter.`in`.web.oauth2

import com.yorizori.yoremo.adapter.`in`.web.constant.YoremoHttpUrl
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.net.URLEncoder

@Component
class OAuth2FailureHandler(
    private val yoremoHttpUrl: YoremoHttpUrl
) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.sendRedirect(
            yoremoHttpUrl.oAuthFailureUrl(
                message = URLEncoder.encode(exception.message, Charsets.UTF_8)
            )
        )
    }
}
