package com.yorizori.yoremo.adapter.`in`.web.config.security.authentication

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.domain.users.port.UsersRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class YoremoDevAuthorizationFilter(
    private val usersRepository: UsersRepository
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION_HEADER = "yoremo-user-id"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val yoremoUserId = request.getHeader(AUTHORIZATION_HEADER)?.toLong()
            ?: return filterChain.doFilter(request, response)

        val yoremoUser = usersRepository.findById(yoremoUserId)
            ?: return filterChain.doFilter(request, response)

        val authentication = YoremoAuthentication(
            email = yoremoUser.email,
            password = "",
            userId = yoremoUserId,
            authority = SimpleGrantedAuthority("ROLE_${yoremoUser.role}")
        )

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                authentication,
                null,
                listOf(authentication.authority)
            )

        filterChain.doFilter(request, response)
    }
}
