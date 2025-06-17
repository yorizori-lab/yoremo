package com.yorizori.yoremo.domain.shoppingcart.port

import com.yorizori.yoremo.adapter.out.persistence.shoppingcart.ShoppingCartJpaRepository
import com.yorizori.yoremo.domain.shoppingcart.entity.ShoppingCart
import org.springframework.stereotype.Repository

@Repository
class ShoppingCartRepository(
    private val shoppingCartJpaRepository: ShoppingCartJpaRepository
) {

    fun saveAll(shoppingCarts: List<ShoppingCart>): List<ShoppingCart> {
        return shoppingCartJpaRepository.saveAll(shoppingCarts)
    }

    fun findAllById(cartIds: List<Long>): List<ShoppingCart> {
        return shoppingCartJpaRepository.findAllById(cartIds)
    }

    fun deleteAllById(cartIds: List<Long>) {
        shoppingCartJpaRepository.deleteAllById(cartIds)
    }

    fun findByUserId(userId: Long): List<ShoppingCart> {
        return shoppingCartJpaRepository.findByUserId(userId)
    }

    fun findByUserIdAndPurchased(userId: Long, isPurchased: Boolean): List<ShoppingCart> {
        return shoppingCartJpaRepository.findByUserIdAndPurchased(userId, isPurchased)
    }
}
