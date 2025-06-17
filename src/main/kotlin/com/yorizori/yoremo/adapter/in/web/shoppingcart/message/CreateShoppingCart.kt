package com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message

import java.math.BigDecimal

abstract class CreateShoppingCart {
    data class Request(
        val shoppingCarts: List<ShoppingCartItemRequest>
    )

    data class ShoppingCartItemRequest(
        val foodName: String,
        val totalAmount: BigDecimal,
        val unit: String = "g",
        val notes: String? = null
    )

    data class Response(
        val success: Boolean
    )
}
