package com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message

import java.math.BigDecimal

abstract class UpdateShoppingCart {
    data class Request(
        val shoppingCarts: List<UpdateShoppingCartItemRequest>
    )

    data class UpdateShoppingCartItemRequest(
        val cartId: Long,
        val foodName: String,
        val totalAmount: BigDecimal,
        val unit: String,
        val purchased: Boolean,
        val notes: String? = null
    )

    data class Response(
        val success: Boolean
    )
}
