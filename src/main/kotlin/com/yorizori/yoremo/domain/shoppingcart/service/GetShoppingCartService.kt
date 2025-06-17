package com.yorizori.yoremo.domain.shoppingcart.service

import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.GetShoppingCart
import com.yorizori.yoremo.domain.common.checkAllOwnership
import com.yorizori.yoremo.domain.shoppingcart.port.ShoppingCartRepository
import org.springframework.stereotype.Service

@Service
class GetShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository
) {

    fun get(request: GetShoppingCart.Request, userId: Long): GetShoppingCart.Response {
        val shoppingCarts = if (request.purchased != null) {
            shoppingCartRepository.findByUserIdAndPurchased(userId, request.purchased)
                .checkAllOwnership(userId)
        } else {
            shoppingCartRepository.findByUserId(userId)
                .checkAllOwnership(userId)
        }

        val responseItems = shoppingCarts.map { shoppingCart ->
            GetShoppingCart.ResponseItem(
                cartId = shoppingCart.cartId!!,
                userId = shoppingCart.userId,
                foodName = shoppingCart.foodName,
                totalAmount = shoppingCart.totalAmount,
                unit = shoppingCart.unit,
                purchased = shoppingCart.purchased,
                notes = shoppingCart.notes,
                createdAt = shoppingCart.createdAt,
                updatedAt = shoppingCart.updatedAt
            )
        }

        return GetShoppingCart.Response(
            shoppingCarts = responseItems
        )
    }
}
