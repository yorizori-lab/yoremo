package com.yorizori.yoremo.test.security

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithYoremoUserSecurityContextFactory : WithSecurityContextFactory<WithYoremoUser> {

    override fun createSecurityContext(annotation: WithYoremoUser): SecurityContext {
        val yoremoAuthentication = YoremoAuthentication(
            email = annotation.email,
            password = annotation.password,
            userId = annotation.userId,
            authority = SimpleGrantedAuthority(annotation.authority)
        )

        val authentication = UsernamePasswordAuthenticationToken(
            yoremoAuthentication,
            yoremoAuthentication.password,
            listOf(yoremoAuthentication.authority)
        )

        return SecurityContextHolder.createEmptyContext()
            .apply { this.authentication = authentication }
    }
}
