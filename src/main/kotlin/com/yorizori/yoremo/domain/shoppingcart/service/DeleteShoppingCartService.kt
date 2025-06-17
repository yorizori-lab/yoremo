package com.yorizori.yoremo.domain.shoppingcart.service

import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.DeleteShoppingCart
import com.yorizori.yoremo.domain.common.checkAllOwnership
import com.yorizori.yoremo.domain.shoppingcart.port.ShoppingCartRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DeleteShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository
) {

    @Transactional
    fun delete(request: DeleteShoppingCart.Request, userId: Long): DeleteShoppingCart.Response {
        shoppingCartRepository.findAllById(request.cartIds)
            .checkAllOwnership(userId)

        shoppingCartRepository.deleteAllById(request.cartIds)

        return DeleteShoppingCart.Response(success = true)
    }
}
