package com.yorizori.yoremo.domain.shoppingcart.entity

import com.yorizori.yoremo.domain.common.Authorizable
import com.yorizori.yoremo.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal

@Entity
@DynamicUpdate
@Table(name = "shopping_cart")
data class ShoppingCart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cartId: Long? = null,

    val userId: Long,

    val foodName: String,

    val totalAmount: BigDecimal,

    val unit: String = "g",

    val purchased: Boolean = false,

    val notes: String? = null
) : BaseEntity(), Authorizable {

    override fun getOwnerId(): Long {
        return this.userId
    }
}
