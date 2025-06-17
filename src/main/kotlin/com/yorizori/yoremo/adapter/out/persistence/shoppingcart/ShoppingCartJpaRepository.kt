package com.yorizori.yoremo.adapter.out.persistence.shoppingcart

import com.yorizori.yoremo.domain.shoppingcart.entity.ShoppingCart
import org.springframework.data.jpa.repository.JpaRepository

interface ShoppingCartJpaRepository : JpaRepository<ShoppingCart, Long> {
    fun findByUserId(userId: Long): List<ShoppingCart>

    fun findByUserIdAndPurchased(userId: Long, isPurchased: Boolean): List<ShoppingCart>
}
