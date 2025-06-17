package com.yorizori.yoremo.adapter.`in`.web.shoppingcart

import com.yorizori.yoremo.adapter.`in`.web.config.security.YoremoAuthentication
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.CreateShoppingCart
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.DeleteShoppingCart
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.GetShoppingCart
import com.yorizori.yoremo.adapter.`in`.web.shoppingcart.message.UpdateShoppingCart
import com.yorizori.yoremo.domain.shoppingcart.service.CreateShoppingCartService
import com.yorizori.yoremo.domain.shoppingcart.service.DeleteShoppingCartService
import com.yorizori.yoremo.domain.shoppingcart.service.GetShoppingCartService
import com.yorizori.yoremo.domain.shoppingcart.service.UpdateShoppingCartService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/shopping-cart/v1")
class ShoppingCartController(
    private val createShoppingCartService: CreateShoppingCartService,
    private val updateShoppingCartService: UpdateShoppingCartService,
    private val deleteShoppingCartService: DeleteShoppingCartService,
    private val getShoppingCartService: GetShoppingCartService
) {

    @PostMapping("/shopping-carts")
    fun create(
        @RequestBody request: CreateShoppingCart.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): CreateShoppingCart.Response {
        return createShoppingCartService.create(request, authentication.userId)
    }

    @PutMapping("/shopping-carts")
    fun update(
        @RequestBody request: UpdateShoppingCart.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): UpdateShoppingCart.Response {
        return updateShoppingCartService.update(request, authentication.userId)
    }

    @DeleteMapping("/shopping-carts")
    fun delete(
        @RequestBody request: DeleteShoppingCart.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): DeleteShoppingCart.Response {
        return deleteShoppingCartService.delete(request, authentication.userId)
    }

    @GetMapping("/shopping-carts")
    fun get(
        request: GetShoppingCart.Request,
        @AuthenticationPrincipal authentication: YoremoAuthentication
    ): GetShoppingCart.Response {
        return getShoppingCartService.get(request, authentication.userId)
    }
}
