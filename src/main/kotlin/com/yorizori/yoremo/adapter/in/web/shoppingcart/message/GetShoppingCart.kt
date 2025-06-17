package com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message

import java.math.BigDecimal
import java.time.Instant

abstract class GetShoppingCart {
    data class Request(
        val purchased: Boolean? = null
    )

    data class Response(
        val shoppingCarts: List<ResponseItem>
    )

    data class ResponseItem(
        val cartId: Long,
        val userId: Long,
        val foodName: String,
        val totalAmount: BigDecimal,
        val unit: String,
        val purchased: Boolean,
        val notes: String?,
        val createdAt: Instant,
        val updatedAt: Instant
    )
}
