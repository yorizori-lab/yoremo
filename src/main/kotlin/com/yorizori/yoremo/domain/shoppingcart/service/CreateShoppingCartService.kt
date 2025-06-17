package com.yorizori.yoremo.domain.shoppingcart.service

import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.CreateShoppingCart
import com.yorizori.yoremo.domain.shoppingcart.entity.ShoppingCart
import com.yorizori.yoremo.domain.shoppingcart.port.ShoppingCartRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CreateShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository
) {

    @Transactional
    fun create(request: CreateShoppingCart.Request, userId: Long): CreateShoppingCart.Response {
        val shoppingCart = request.shoppingCarts.map { shoppingCart ->
            ShoppingCart(
                userId = userId,
                foodName = shoppingCart.foodName,
                totalAmount = shoppingCart.totalAmount,
                unit = shoppingCart.unit,
                notes = shoppingCart.notes
            )
        }

        shoppingCartRepository.saveAll(shoppingCart)

        return CreateShoppingCart.Response(success = true)
    }
}
