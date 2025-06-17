package com.yorizori.yoremo.domain.shoppingcart.service

import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.UpdateShoppingCart
import com.yorizori.yoremo.domain.common.checkAllOwnership
import com.yorizori.yoremo.domain.shoppingcart.port.ShoppingCartRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UpdateShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository
) {

    @Transactional
    fun update(request: UpdateShoppingCart.Request, userId: Long): UpdateShoppingCart.Response {
        val existingCarts = shoppingCartRepository.findAllById(
            request.shoppingCarts.map { it.cartId }
        ).checkAllOwnership(userId)

        val existingCartsMap = existingCarts.associateBy { it.cartId }

        val updatedCarts = request.shoppingCarts.map { updateCart ->
            val existingCart = existingCartsMap[updateCart.cartId]!!

            existingCart.copy(
                foodName = updateCart.foodName,
                totalAmount = updateCart.totalAmount,
                unit = updateCart.unit,
                purchased = updateCart.purchased,
                notes = updateCart.notes
            )
        }

        shoppingCartRepository.saveAll(updatedCarts)

        return UpdateShoppingCart.Response(success = true)
    }
}
