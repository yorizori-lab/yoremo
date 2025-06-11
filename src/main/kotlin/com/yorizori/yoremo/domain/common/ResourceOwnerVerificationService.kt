package com.yorizori.yoremo.domain.common

import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.support.Repositories
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass

@Service
@Transactional(readOnly = true)
class ResourceOwnerVerificationService(
    applicationContext: ApplicationContext,
) {

    private val repositories: Repositories = Repositories(applicationContext)

    fun <T : Authorizable> checkAndGet(
        entity: KClass<T>,
        userId: Long,
        resourceId: Long
    ): T? {
        val repository = repositories.getRepositoryFor(entity.java).getOrNull()

        @Suppress("unchecked_cast")
        val resource = (repository as JpaRepository<T, Long>).findById(resourceId).getOrNull()

        if (resource == null) {
            return null
        }

        if (resource.getOwnerId() != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.")
        }

        return resource
    }
}
