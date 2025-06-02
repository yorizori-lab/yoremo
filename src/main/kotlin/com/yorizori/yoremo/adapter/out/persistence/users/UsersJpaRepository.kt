package com.yorizori.yoremo.adapter.out.persistence.users

import com.yorizori.yoremo.domain.users.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UsersJpaRepository : JpaRepository<Users, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Users?
}
