package com.yorizori.yoremo.adapter.`in`.web.config

import com.google.common.base.CaseFormat
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Collections
import java.util.Enumeration

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ConvertSnakeToCamelCaseFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val snakeCaseMap = mutableMapOf<String, Array<String>>()

        request.parameterMap.forEach { (key, values) ->
            if (key.contains("_")) {
                snakeCaseMap[CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key)] = values
            } else {
                snakeCaseMap[key] = values
            }
        }

        filterChain.doFilter(
            object : HttpServletRequestWrapper(request) {
                override fun getParameterMap(): Map<String, Array<String>?> {
                    return snakeCaseMap
                }

                override fun getParameter(name: String?): String? {
                    return snakeCaseMap[name]?.get(0)
                }

                override fun getParameterNames(): Enumeration<String?> {
                    return Collections.enumeration(snakeCaseMap.keys.toList())
                }

                override fun getParameterValues(name: String?): Array<String>? {
                    return snakeCaseMap[name]
                }
            },
            response
        )
    }
}
