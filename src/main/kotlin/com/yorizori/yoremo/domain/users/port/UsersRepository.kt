package com.yorizori.yoremo.domain.users.port

import com.yorizori.yoremo.adapter.out.persistence.users.UsersJpaRepository
import com.yorizori.yoremo.domain.users.entity.Users
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class UsersRepository(
    private val usersJpaRepository: UsersJpaRepository
) {
    fun findById(id: Long): Users? {
        return usersJpaRepository.findById(id).getOrNull()
    }

    fun existsByEmail(email: String): Boolean {
        return usersJpaRepository.existsByEmail(email)
    }

    fun findByEmail(email: String): Users? {
        return usersJpaRepository.findByEmail(email)
    }

    fun save(users: Users): Users {
        return usersJpaRepository.save(users)
    }
}
