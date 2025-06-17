package com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message

abstract class DeleteShoppingCart {
    data class Request(
        val cartIds: List<Long>
    )

    data class Response(
        val success: Boolean
    )
}
