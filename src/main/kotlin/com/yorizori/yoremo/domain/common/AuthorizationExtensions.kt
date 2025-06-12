package com.yorizori.yoremo.domain.common

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun <T: Authorizable> T?.checkOwnership(
    userId: Long,
): T {
    if (this == null) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.")
    }

    if (this.getOwnerId() != userId) {
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.")
    }

    return this
}

fun <T: Authorizable> List<T>.checkAllOwnership(
    userId: Long,
): List<T> {
    if (this.isEmpty()) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.")
    }

    val unauthorizedEntities = this.filter { it.getOwnerId() != userId }
    if (unauthorizedEntities.isNotEmpty()) {
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.")
    }

    return this
}
